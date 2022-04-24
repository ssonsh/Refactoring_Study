# 리팩토링 21. 파생 변수를 질의 함수로 바꾸기

**냄새** : 가변 데이터

→ **“파생 변수를 질의 함수로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**파생 변수를 질의 함수로 바꾸기 Replace Derived Variable with Query**

- 변경할 수 있는 데이터를 최대한 줄이도록 노력해야 한다.
- **`계산해서 알아낼 수 있는 변수는 제거할 수 있다.`**
    - 계산 자체가 데이터의 의미를 잘 표현하는 경우도 있다.
    - 해당 변수가 어디선가 잘못된 값으로 수정될 수 있는 가능성을 제거할 수 있다.
- **`계산에 필요한 데이터`**가 변하지 않는 값이라면?
    - 계산의 **`결과에 해당하는 데이터 역시 불변`** 데이터 이기 때문에
    - 해당 변수는 그대로 유지할 수 있다.
    

---

아래 코드를 확인해보자.

**Discount.java**

- 여기서 `**discountedTotal**` 필드는 “**discount**”, “**baseTotal**” 필드에 의해 파생된 변수임을 알 수 있다.
    - 계산은 setDiscount 메소드를 호출해야 알 수 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._21_replace_derived_variable_with_query;

public class Discount {

    private double discountedTotal;
    private double discount;

    private double baseTotal;

    public Discount(double baseTotal) {
        this.baseTotal = baseTotal;
    }

    public double getDiscountedTotal() {
        return this.discountedTotal;
    }

    public void setDiscount(double number) {
        this.discount = number;
        this.discountedTotal = this.baseTotal - this.discount;
    }
}
```

```java
@Test
void discount() {
    Discount discount = new Discount(100);
//        assertEquals(100, discount.getDiscountedTotal());

    discount.setDiscount(10);
    assertEquals(90, discount.getDiscountedTotal());
}
```

discountTotal 필드는 setDiscount 메소드 호출을 통해

- baseTotal 필드 값 - discount 필드 값의 결과로 설정된다.
- 이 설정된 discountTotal 값은 getDiscountedToal() 메서드를 통해 확인할 수 있다.

위와 같이 정의된 경우 setDiscount 메소드를 호출하지 않은 상태에서

getDiscountedTotal() 메서드를 호출하게 되면 원하는 결과를 얻지 못할 것이다.

즉 뮤터블 discountTotal 필드에 대해 리팩토링을 할 수 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._21_replace_derived_variable_with_query;

public class Discount {

    private double discount;
    private double baseTotal;

    public Discount(double baseTotal) {
        this.baseTotal = baseTotal;
    }

    public double getDiscountedTotal() {
        return this.baseTotal - this.discount;
    }

    public void setDiscount(double number) {
        this.discount = number;
    }
}
```

setDiscount를 통해 discountedTotal 값을 변경하던 부분을

getDiscountedTotal() 쿼리하는 메소드 내에서 계산하여 반환하도록 한다면

- discountedTotal 이라는 필드 자체가 제거될 수 있다.

또한, set되지 않아서 조회 시 원하는 결과를 얻지 못한 테스트 코드도 정상적으로 처리될 수 있다.

---

아래 코드를 보며 다시 알아보자

**ProductionPlan.java**

- 간단한 adjustment 합계를 산출하는 메소드가 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._21_replace_derived_variable_with_query;

import java.util.ArrayList;
import java.util.List;

public class ProductionPlan {

    private double production;
    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
        this.production += adjustment;
    }

    public double getProduction() {
        return this.production;
    }
}
```

applyAdjustment 메소드 내부를 보자.

adjustments 라는 컬랙션에 adjustment를 add 하고 있고

추가적으로 production 필드에 합계를 산출해내고 있다.

  
    🎈 여기서 production 필드가 Derived Variable 이다.


여기서 adjustments 컬랙션에 add하고 있기 때문에 production 필드에 합계를 산출해내고 있을 필요가 없다.

- 조회하는 메소드에서 최종 산출하여 보여줄 수 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._21_replace_derived_variable_with_query;

import java.util.ArrayList;
import java.util.List;

public class ProductionPlan {

    private List<Double> adjustments = new ArrayList<>();

    public void applyAdjustment(double adjustment) {
        this.adjustments.add(adjustment);
    }

    public double getProduction() {
        // adjustments.stream().reduce((double) 0, Double::sum);
        return adjustments.stream().mapToDouble(Double::valueOf).sum();
    }
}
```

위와 같이 Query하는 메소드인 getProduction() 내부에서

값을 산출하여 반환하도록 함으로써 Derived Variable 인 production 필드 자체를 없앨 수 있다.

다시 짚고 간다면, Derived Variable은 파생변수이며,

파생변수는 어떤 값들에 의해 만들어진 변수라고 보면된다.

- 위 케이스에서는 adjustment의 누적된 값이 production에 할당되고 있다.
- 즉, production은 adjustment 들에 의해 만들어진 Derived Variable 이다.
