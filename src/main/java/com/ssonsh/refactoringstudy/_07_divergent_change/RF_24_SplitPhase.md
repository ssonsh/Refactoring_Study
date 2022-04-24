# 리팩토링 24. 단계 쪼개기

**냄새** : 뒤엉킨 변경

→ **“단계 쪼개기”** 를 통해 리팩토링 할 수 있다.

**단계 쪼개기 Split Phase**

- **서로 다른 일**을 하는 코드가 각기 **`다른 모듈`**로 분리되도록 한다.
    - 그래야 어떤 것을 변경해야 할 때, 그것과 관련있는 것만 신경쓸 수 있다.
- 여러 일을 하는 함수의 처리 과정을 각기 다른 단계로 구분할 수 있다.
    - 예
        - 전처리 → 핵심 비즈니스 요구사항 → 후처리
        - 컴파일러 : 텍스트 읽어오기 → 실행가능한 형태로 변경하기
- ***서로 다른 데이터를 사용한다면 단계를 나누는데 있어 중요한 단서가 될 수 있다.***
- **중간 데이터(intermediate Data)** 를 만들어 단계를 구분하고 매개변수를 줄이는데 활용할 수 있다.
    - 쪼갠 메서드간의 데이터 전달이 필요할 때 “**중간 데이터(intermediate Data)**”를 활용할 수 있다.

---

아래 코드를 기반으로 확인해보자.

**Product.java**

```java
package com.ssonsh.refactoringstudy._07_divergent_change._24_split_phase;

public record Product(double basePrice, double discountThreshold, double discountRate) {

}
```

**ShippingMethod.java**

```java
package com.ssonsh.refactoringstudy._07_divergent_change._24_split_phase;

public record ShippingMethod(double discountThreshold, double discountedFee, double feePerCase) {
}
```

**PriceOrder.java**

```java
package com.ssonsh.refactoringstudy._07_divergent_change._24_split_phase;

public class PriceOrder {

    public double priceOrder(Product product, int quantity, ShippingMethod shippingMethod) {
        final double basePrice = product.basePrice() * quantity;
        final double discount = Math.max(quantity - product.discountThreshold(), 0)
                                * product.basePrice() * product.discountRate();
        final double shippingPerCase = (basePrice > shippingMethod.discountThreshold()) ?
                                       shippingMethod.discountedFee() : shippingMethod.feePerCase();
        final double shippingCost = quantity * shippingPerCase;
        final double price = basePrice - discount + shippingCost;
        return price;
    }
}
```

**Test.java**

```java
package com.ssonsh.refactoringstudy._24_split_phase;

import com.ssonsh.refactoringstudy._07_divergent_change._24_split_phase.PriceOrder;
import com.ssonsh.refactoringstudy._07_divergent_change._24_split_phase.Product;
import com.ssonsh.refactoringstudy._07_divergent_change._24_split_phase.ShippingMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceOrderTest {

    @Test
    void priceOrder_discountedFee() {
        PriceOrder priceOrder = new PriceOrder();
        double price = priceOrder.priceOrder(new Product(10, 2, 0.5),
                4,
                new ShippingMethod(20, 1, 5));

        // basePrice = 10 * 4 = 40
        // discount = 2 * 10 * 0.5 = 10
        // shippingPerCase = 1 (40 > 20)
        // shippingCost = 4 * 1 = 4
        // price = 40 - 10 + 4 = 34

        assertEquals(34, price);
    }

    @Test
    void priceOrder_feePerCase() {
        PriceOrder priceOrder = new PriceOrder();
        double price = priceOrder.priceOrder(new Product(10, 2, 0.5),
                2,
                new ShippingMethod(20, 1, 5));

        // basePrice = 10 * 2 = 20
        // discount = 0 * 10 * 0.5 = 0
        // shippingPerCase = 5
        // shippingCost = 2 * 5 = 10
        // price = 20 - 0 + 10 = 30

        assertEquals(30, price);
    }

}
```

**PriceOrder 클래스의 내부 로직을 확인해보자.**

- basePrice 기본 단가를 구해내고
- discount 할인정보를 구한다.
- shippingPerCase 배송비 연산에 필요한 정보를 구해내고
- shippingCoast 배송비를 산출해낸다.
- 최종적으로 기본 단가와 할인정보, 배송비를 합쳐 결제해야 할 금액을 산출해낸다.

우선 배송비와 관련한 처리를 하는 부분을 메소드로 추출해낼 수 있다.

→ applyShipping 

메소드로 추출하면서 전달되는 메소드를 줄이기 위해 PriceData 라는 중간 데이터를 만들어 전달시켜줄 수 있다.

```java
final PriceData priceData = new PriceData(basePrice, discount, quantity);

final double price = applyShipping(priceData, shippingMethod);

private double applyShipping(PriceData priceData,
                             ShippingMethod shippingMethod) {
    final double shippingPerCase = (priceData.basePrice() > shippingMethod.discountThreshold()) ?
                                   shippingMethod.discountedFee() : shippingMethod.feePerCase();
    final double shippingCost = priceData.quantity() * shippingPerCase;
    final double price = priceData.basePrice() - priceData.discount() + shippingCost;
    return price;
}
```

또한 PriceData의 기반이 되는 basePrice와 discount 를 산출하는 로직을 하나의 메소드로 추출하여

PriceData 중간 데이터를 반환하도록 한다면 다시 한번 문맥을 묶어낼 수 있다.

→ calculatePriceData

```java
final PriceData priceData = calculatePriceData(product, quantity);

private PriceData calculatePriceData(Product product, int quantity) {
    final double basePrice = product.basePrice() * quantity;
    final double discount = Math.max(quantity - product.discountThreshold(), 0)
                            * product.basePrice() * product.discountRate();

    final PriceData priceData = new PriceData(basePrice, discount, quantity);
    return priceData;
}
```

최종적으로 PriceOrder에서 price 최종 결제 값을 산출하는 메소드가 아래와 같이 축소된다.
```java
public double priceOrder(Product product, int quantity, ShippingMethod shippingMethod) {
    final PriceData priceData = calculatePriceData(product, quantity);
    final double price = applyShipping(priceData, shippingMethod);
    return price;
}
```
