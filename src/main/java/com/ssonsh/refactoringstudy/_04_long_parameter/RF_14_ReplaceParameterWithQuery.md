# 리팩토링 14. 매개변수를 질의 함수로 바꾸기

**냄새** : 긴 매개변수 목록

→ **“매개변수를 질의 함수로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**매개변수를 질의 함수로 바꾸기 Replace Parameter with Query**

- 함수의 매개변수 목록은 함수의 다양성을 대변하며, 짧을수록 이해하기 좋다.
- **어떤 한 매개변수를 다른 매개변수를 통해 알아낼 수 있다면?**
    - 중복 매개변수라 생각할 수 있다.
- 매개변수에 값을 전달하는 것은 “함수를 호출하는 쪽”의 책임이다.
    - ***가능하면 함수를 호출하는 쪽의 책임을 줄이고 함수 내부에서 책임지도록 노력한다.***
- “**`임시 변수를 질의 함수로 바꾸기`**”와 “**`함수 선언 변경하기`**”를 통해 이 리팩토링을 적용한다.
    - [리팩토링 7. 임시 변수를 질의 함수로 바꾸기](https://www.notion.so/7-88f4943280864f31b9a5079811d82bc0)
    - [리팩토링 1. 함수 선언 변경하기](https://www.notion.so/1-7c8c6ac9febe45f581c1c872f60f0faa)


---

아래 코드를 보자

- 할인율을 계산하는 코드이며
- 할인율이 적용된 가격인 discountPrice 메소드의 결과는
- finalPrice 메서드의 결과를 바탕으로 만들어진다.

```java
package com.ssonsh.refactoringstudy._04_long_parameter;

public class Order {

    private int quantity;

    private double itemPrice;

    public Order(int quantity, double itemPrice) {
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public double finalPrice() {
        double basePrice = this.quantity * this.itemPrice;
        int discountLevel = this.quantity > 100 ? 2 : 1;
        return this.discountedPrice(basePrice, discountLevel);
    }

    private double discountedPrice(double basePrice, int discountLevel) {
        return discountLevel == 2 ? basePrice * 0.9 : basePrice * 0.95;
    }
}
```

테스트 코드

```java
package com.ssonsh.refactoringstudy._04_long_parameter_list._01_before;

import com.ssonsh.refactoringstudy._04_long_parameter.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @Test
    void discountedPriceWithDiscountLevel2() {
        int quantity = 200;
        double price = 100;
        assertEquals(quantity * price * 0.90, new Order(quantity, price).finalPrice());
    }

    @Test
    void discountedPriceWithDiscountLevel1() {
        int quantity = 100;
        double price = 100;
        assertEquals(quantity * price * 0.95, new Order(quantity, price).finalPrice());
    }

}
```

---

**리팩토링을 시작해보자.**

우선, finalPrice 메서드의 discountLevel을 산출하는 구문을 메소드로 추출해보자.

```java
public double finalPrice() {
    double basePrice = this.quantity * this.itemPrice;
    int discountLevel = discountLevel();
    return this.discountedPrice(basePrice, discountLevel);
}

private int discountLevel() {
  return this.quantity > 100 ? 2 : 1;
}

private double discountedPrice(double basePrice, int discountLevel) {
    return discountLevel == 2 ? basePrice * 0.9 : basePrice * 0.95;
}
```

- **`discountLevel`** 이라는 메소드로 추출되었음으로
- 위에서 **discountPrice** 메소드를 호출할 때 discountLevel 값을 굳이 매개변수로 넘기지 않아도 된다.

discountedPrice 메소드에서 int discountLevel 매개변수를 제거해주고

- 내부에서 사용하는 discountLevel 값은 위에서 추출한 메소드를 직접 호출하여 사용한다.
```java
public double finalPrice() {
double basePrice = this.quantity * this.itemPrice;
return this.discountedPrice(basePrice);
}

private int discountLevel() {
return this.quantity > 100 ? 2 : 1;
}

private double discountedPrice(double basePrice) {
return discountLevel() == 2 ? basePrice * 0.9 : basePrice * 0.95;
}
```