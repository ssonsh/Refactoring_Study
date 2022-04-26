# 리팩토링 35. 죽은 코드 제거하기

**냄새** : 추측성 일반화

→ **“죽은 코드 제거하기”** 를 통해 리팩토링 할 수 있다.

**죽은 코드 제거하기 Remove Declaration**

- 사용하지 않는 코드가 애플리케이션 성능이나 기능에 영향을 끼치지 않는다.
- 하지만, 해당 소프트웨어가 어떻게 동작하는지 이해하려는 사람들에게는 꽤 고통을 줄 수 있다.
- 실제로는 나중에 필요해질 코드라 하더라도 지금 쓰이지 않으면 삭제하라.
    - 주석으로 감싸는게 아니라 제거이다.
    - 나중에 정말 필요해진다면 git과 같은 버전 관리 시스템을 사용해 복원할 수 있을 것이다.


    💡 사용하지 않는 코드를 제거하는 것이 코드를 이해하려는 사람을 위한 활동이다.

---

아래 코드를 통해 살펴보자.

Reservation.java

```java
package com.ssonsh.refactoringstudy._15_speculative_generality._35_remove_dead_code;

import java.time.LocalDateTime;

public class Reservation {

    private String title;
    private LocalDateTime from;
    private LocalDateTime to;
    private LocalDateTime alarm;

    public Reservation(String title, LocalDateTime from, LocalDateTime to) {
        this.title = title;
        this.from = from;
        this.to = to;
    }

    public void setAlarmBefore(int minutes) {
        this.alarm = this.from.minusMinutes(minutes);
    }

    public LocalDateTime getAlarm() {
        return alarm;
    }
}
```

IntelliJ 의 설정을 통해 사용되고 있는지 여부를 쉽게 파악할 수 있다.
- Editor -> Inlay Hints  -> Java -> [check!] Show hints for Usages

