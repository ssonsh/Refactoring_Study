# 냄새 19. 내부자 거래

### 내부자 거래  Insider Trading

- 어떤 모듈이 `**다른 모듈의 내부 정보를 지나치게 많이 알고 있는**` 코드 냄새
    - 그로 인해 지나치게 강한 결합도(couping) 가 생길 수 있다.
- 적절한 모듈로 “**함수 옮기기(Move Function)**” 와 “**필드 옮기기(Move Field)**” 를 사용해서 결합도를 낮출 수 있다.
    - [리팩토링 25. 함수 옮기기](https://www.notion.so/25-fcf63963c6c44abd8fb6bb48bb90325e)
    - [리팩토링 27. 필드 옮기기](https://www.notion.so/27-fd1b47cd660f458792776c5503c0c4ad)
- 여러 모듈이 자주 사용하는 공통적인 기능은 새로운 모듈을 만들어 잘 관리하거나
    - “**위임 숨기기(Hide Delegate)**”를 사용해  특정 모듈의 중재자처럼 사용할 수도 있다.
        - [리팩토링 37. 위임 숨기기](https://www.notion.so/37-a8c34214640c4a31a9dbd1c3fcade852)
- 상속으로 인한 결합도를 줄일 때는 “**슈퍼클래스 또는 서브 클래스를 위임으로 교체하기**”를 사용할 수 있다.
    - [리팩토링 39. 슈퍼클래스를 위임으로 바꾸기](https://www.notion.so/39-301dff33f11e4ed394a3f1706d1dabc4)


---

아래 코드를 보며 살펴보자

**Ticket.java**

- prime 이라는 boolean 타입을 통해
- pirme 한 Ticket 인지 아닌지를 판단하고 있다.

```java
package com.ssonsh.refactoringstudy._19_insider_trading;

import java.time.LocalDate;

public class Ticket {

    private LocalDate purchasedDate;

    private boolean prime;

    public Ticket(LocalDate purchasedDate, boolean prime) {
        this.purchasedDate = purchasedDate;
        this.prime = prime;
    }

    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }

    public boolean isPrime() {
        return prime;
    }
}
```

Checkin.java

- Tickcet을 참조하고 있는데,
- Ticket이 fastPass 인지 아닌지 판단하게 된다.
    - ticket 안에 있는 내부 정도를 많이 활용하고 있다.
        - isPrime()
        - getPurchasedDate().isBefore()
-

```java
package com.ssonsh.refactoringstudy._19_insider_trading;

import java.time.LocalDate;

public class CheckIn {

    public boolean isFastPass(Ticket ticket) {
        LocalDate earlyBirdDate = LocalDate.of(2022, 1, 1);
        return ticket.isPrime() && ticket.getPurchasedDate().isBefore(earlyBirdDate);
    }
}
```

**이 경우 Checkin 에 있는 것이 아니라 Ticket 쪽으로 옮겨가는게 더 적절할 수 있다.**

- 이렇게 되면 Ticket에서 fastPass 를 확인해낼 수 있고
- 가장 가까운 필드를 이용해 처리해낼 수 있다.
- 자연스럽게 Checkin 클래스는 없어질 수 있다.

```java
package com.ssonsh.refactoringstudy._19_insider_trading;

import java.time.LocalDate;

public class Ticket {

    private LocalDate purchasedDate;

    private boolean prime;

    public Ticket(LocalDate purchasedDate, boolean prime) {
        this.purchasedDate = purchasedDate;
        this.prime = prime;
    }

    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }

    public boolean isPrime() {
        return prime;
    }

    public boolean isFastPass() {
        LocalDate earlyBirdDate = LocalDate.of(2022, 1, 1);
        return isPrime() && getPurchasedDate().isBefore(earlyBirdDate);
    }
}
```

```java
package com.ssonsh.refactoringstudy._19_insider_trading;

import java.time.LocalDate;

public class CheckIn {

}
```

더이상 CheckIn에서 Ticket 내부 정보를 꺼내어 참조하는 일이 사라지고

결합도가 없어지게 된다.

- 이건 극단적인 케이스이고
- CheckIn에는 다른 코드들이 남아있게 될 것이다.