# 리팩토링 34. 계층 합치기

**냄새** : 성의없는 요소

→ **“계층 합치기”** 를 통해 리팩토링 할 수 있다.

**계층 합치기 Collapse Hierachy**

- 상속 구조를 리팩토링하는 중에 기능을 올리고 내리다보면 하위 클래스와 상위 클래스 코드에 차이가 없는 경우가 발생할 수 있다.
    - 그런 경우에 그 둘을 합칠 수 있다.
- 하위 클래스와 상위클래스 중에 어떤 것을 없애야 하는가?
    - 둘 중에 보다 이름이 적절한 쪽을 선택하지만,
    - 애매하다면 어느 쪽을 선택해도 상관없다.

---

아래 코드를 살펴보자

Reservation.java

```java
package com.ssonsh.refactoringstudy._14_lazy_elements._34_collapse_hierarchy;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private List<String> members;
    private String owner;
    private boolean paid;
}
```

[CourtReservation.java](http://CourtReservation.java) (extends Reservation)

```java
package com.ssonsh.refactoringstudy._14_lazy_elements._34_collapse_hierarchy;

public class CourtReservation extends Reservation {
    private String courtNumber;
}
```

위와 같은 경우 courtReservation에 있는 courtNumber 의 용도에 따라

굳이 상속관계를 유지하는 것이 아니라 상위 클래스나, 하위 클래스로 Collapse 할 수 있다.

조금 더 가치 있는 의미를 표현하는 클래스를 기능으로 기준을 잡고

**`Pull Down`** 혹은 **`Pull Up`**을 이용하여 옮겨서 리팩토링을 진행할 수 있다.