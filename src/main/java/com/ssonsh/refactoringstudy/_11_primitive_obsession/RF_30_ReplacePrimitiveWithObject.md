# 리팩토링 30. 기본형을 객체로 바꾸기

**냄새** : 기본형 집착

→ **“기본형을 객체로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**기본형을 객체로 바꾸기 Replace Primitive with Object**

- 개발 초기에는 기본형(숫자 또는 문자열)으로 표현한 데이터가 나중에는 해당하는 데이터와 관련있는 다양한 기능을 필요로 하는 경우가 발생한다.
    - 예)
        - 문자열로 표현하던 전화번호의 지역 코드가 필요하다거나 다양한 포맷을 지원하는 경우
        - 숫자로 표현하던 온도의 단위 (화씨, 섭씨)를 변환하는 경우
- 기본형을 사용한 데이터를 감싸 줄 클래스를 만들면, 필요한 기능을 추가할 수 있다.


    💡 처음부터 이런 요구사항을 만족하도록 설계하는 것은 실질적으로 불가능한 경우가 많다.
    즉, 처음에 만들어진 상황에서 향후 변경될 가능성들을 예측하고 대응함으로써 리팩토링 해나갈 수 있다.


---

아래 코드를 보며 살펴보자.

Order.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object;

public class Order {

    private String priority;

    public Order(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }
}
```

OrderProcessor.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object;

import java.util.List;

public class OrderProcessor {

    public long numberOfHighPriorityOrders(List<Order> orders) {
        return orders.stream()
                .filter(o -> o.getPriority() == "high" || o.getPriority() == "rush")
                .count();
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._30_repliace_primitive_with_object;

import com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object.Order;
import com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object.OrderProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProcessorTest {

    @Test
    void numberOfHighPriorityOrders() {
        OrderProcessor orderProcessor = new OrderProcessor();
        long highPriorityOrders = orderProcessor.numberOfHighPriorityOrders(
                List.of(new Order("low"),
                        new Order("normal"),
                        new Order("high"),
                        new Order("rush")));
        assertEquals(2, highPriorityOrders);
    }

}
```

**주문(Order)**에 우선순위(priority)가 있다고 가정하고 있다.

- 이 priority는 String 문자열로 표현하고 있다.

**주문처리기(OrderProcessor)**에서는 우선순위가 높은 주문이 얼마나 있는지 판단하는 **numberOfHighPriorityOrders(..)** 라는 메서드가 있다.

- Order의 목록을 가져와서
- high, rush 에 해당하는 주문을 조회하는 로직이 작성되어 있다.

우선순위를 위한 객체를 Primitive Type으로 사용하지 말고

객체로 감싸서 구현해보자.

**Priority.java**

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._30_repliace_primitive_with_object;

import java.util.List;

public class Priority {

    private String value;

    // Priority 객체의 값에 대해 Type Safty를 보장하기 위해 허용가능한 값을 선언하여
    // 활용할 수 있다.
    private List<String> legalValues = List.of("low", "normal", "high", "rush");

    public Priority(String value) {
        if(legalValues.contains(value)){
            this.value = value;
        }else{
            throw new IllegalArgumentException("illegal value for priority " + value);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    private int index(){
        return this.legalValues.indexOf(this.value);
    }

    public boolean higherThan(Priority other){
        return this.index() > other.index();
    }
}
```

- 여기서 기존의 String 타입으로 활용했던 priority와 변경된 것과 비교를 해볼 수 있따.
- 기존의 경우 String 형태로 선언되어 있었기 때문에 **`타입 세이프티`** 하지 못하다.
    - 타입 세이프티 하지 못하다는 말은,
    - 해당 필드에 할당될 수 있는 값이 정해져 있지만, 다른 값을 할당한다고 해서 할당 그 자체 행위에서 막아내지 못한다.
    - 이런 방법을 객체로 바꿈으로써 해결해낼 수 있다.

- 또한, Client단, 활용단에서 해당 필드에 대한 특정 값들을 직접적으로 활용했어야 하지만, 객체로 변환하여 객채 내에서 제공하는 기능을 통해 원하는 행위를 수행해낼 수 있다.