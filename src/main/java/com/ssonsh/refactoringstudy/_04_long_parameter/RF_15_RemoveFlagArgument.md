# 리팩토링 15. 플래그 인수 제거하기

**냄새** : 긴 매개변수 목록

→ **“플래그 인수 제거하기”** 를 통해 리팩토링 할 수 있다.

**플래그 인수 제거하기 Remove Flag Argument**

- 플래그는 보통 함수에 매개변수로 전달해서, 함수 내부의 로직을 분기하는데 사용한다.
- 플래그를 사용한 함수는 차이를 파악하기 어렵다.
    - bookConcert(customer, flase) | bookConcert(customer, true)
    - bookConcert(customer) | premiumBookConcert(customer)
- **조건문 분해하기 (Decompose Condition)**을 활용할 수 있다.

<aside>
🎈 플래그는 꼭 Boolean 타입이 아니더라도 Enum, String 등 다양할 수 있다.
역할 자체가 플래그의 성격을 가지는 것을 통칭한다고 보자

</aside>

---

**아래 코드를 기준으로 살펴보자.**

- Shipment의 deliveryDate 메서드가 있고 이 메서드에 true, false  플래그 값을 전달받아 특정 로직을 수행한다.
- 사용하는 클라이언트 코드를 보면 아래와 같다.

    ```java
    assertEquals(placedOn.plusDays(1), shipment.deliveryDate(orderFromWA, true));
    assertEquals(placedOn.plusDays(2), shipment.deliveryDate(orderFromWA, false));
    ```

- 위 클라이언트 코드만 보면 deliveryDate 메서드의 true/false 로 전달되는 매개변수가 어떤 일을 하는지 유추하기 어렵다.

Order.java

```java
package com.ssonsh.refactoringstudy._04_long_parameter._15_remove_flag_argument;

import java.time.LocalDate;

public class Order {

    private LocalDate placedOn;
    private String deliveryState;

    public Order(LocalDate placedOn, String deliveryState) {
        this.placedOn = placedOn;
        this.deliveryState = deliveryState;
    }

    public LocalDate getPlacedOn() {
        return placedOn;
    }

    public String getDeliveryState() {
        return deliveryState;
    }
}
```

Shipment.java

```java
package com.ssonsh.refactoringstudy._04_long_parameter._15_remove_flag_argument;

import java.time.LocalDate;

public class Shipment {

    public LocalDate deliveryDate(Order order, boolean isRush) {
        if (isRush) {
            int deliveryTime = switch (order.getDeliveryState()) {
                case "WA", "CA", "OR" -> 1;
                case "TX", "NY", "FL" -> 2;
                default -> 3;
            };
            return order.getPlacedOn().plusDays(deliveryTime);
        } else {
            int deliveryTime = switch (order.getDeliveryState()) {
                case "WA", "CA" -> 2;
                case "OR", "TX", "NY" -> 3;
                default -> 4;
            };
            return order.getPlacedOn().plusDays(deliveryTime);
        }
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._04_long_parameter_list._15_remove_flag_argument;

import com.ssonsh.refactoringstudy._04_long_parameter._15_remove_flag_argument.Order;
import com.ssonsh.refactoringstudy._04_long_parameter._15_remove_flag_argument.Shipment;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipmentTest {

    @Test
    void deliveryDate() {
        LocalDate placedOn = LocalDate.of(2021, 12, 15);
        Order orderFromWA = new Order(placedOn, "WA");

        Shipment shipment = new Shipment();
        assertEquals(placedOn.plusDays(1), shipment.deliveryDate(orderFromWA, true));
        assertEquals(placedOn.plusDays(2), shipment.deliveryDate(orderFromWA, false));
    }

}
```

---

[**Shipment.java](http://Shipment.java) 의 deliveryDate 메서드를 리팩토링 해보자.**

- devlieryDate 메서드 내부의 isRush 플래그 값에 따른 if/else body 영역을
- 조건문 분해하기 기술을 이용하여 메소드로 추출하였다.

```java
package com.ssonsh.refactoringstudy._04_long_parameter._15_remove_flag_argument;

import java.time.LocalDate;

public class Shipment {

    public LocalDate deliveryDate(Order order, boolean isRush) {
        if (isRush) {
            return rushDeliveryDate(order);
        } else {
            return regularDeliveryDate(order);
        }
    }

    public LocalDate regularDeliveryDate(Order order) {
        int deliveryTime = switch (order.getDeliveryState()) {
            case "WA", "CA" -> 2;
            case "OR", "TX", "NY" -> 3;
            default -> 4;
        };
        return order.getPlacedOn().plusDays(deliveryTime);
    }

    public LocalDate rushDeliveryDate(Order order) {
        int deliveryTime = switch (order.getDeliveryState()) {
            case "WA", "CA", "OR" -> 1;
            case "TX", "NY", "FL" -> 2;
            default -> 3;
        };
        return order.getPlacedOn().plusDays(deliveryTime);
    }
}
```

**이제 클라이언트 코드를 수정해보자.**

- 추출된 메서드를 호출하도록 한다.
    - 역할에 맞는 메서드로 분리되었음으로 클라이언트단에서 명확히 원하는 메서드를 호출하는 것이다.

```java
assertEquals(placedOn.plusDays(1), shipment.rushDeliveryDate(orderFromWA));
assertEquals(placedOn.plusDays(2), shipment.regularDeliveryDate(orderFromWA));
```

**이후 Shipment.java에 최초 devlieryDate 메서드는 사용할 곳이 없으니 제거해줄 수 있다.**