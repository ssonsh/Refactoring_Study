# 리팩토링 28. 함수 인라인

**냄새** : 산탄총 수술

→ **“함수 인라인”** 를 통해 리팩토링 할 수 있다.

**함수 인라인 Inline Function**

- “**함수 추출하기(Extract Function)**”의 반대에 해당하는 리팩토링
    - 함수로 추출하여 함수 이름으로 의도는 표현하는 방법
    - [리팩토링 4. 함수 추출하기](https://www.notion.so/4-42dc95cdb4744117805ca807fa4db63e)
- 간혹, 함수 본문이 함수 이름 만큼 또는 그보다 더 잘 의도를 표현하는 경우도 있다.
- 함수 리팩토링이 잘못된 경우에 여러 함수를 인라인하여 커다란 함수를 만든 다음에 다시 함수 추출하기를 시도할 수도 있다.
- **단순히 메소드 호출을 감싸는 우회형(indirection) 메서드라면 인라인으로 없앨 수 있다.**
- 단, 상속 구조에서 오버라이딩 하고 있는 메소드는 인라인 할 수 없다.

---

아래 코드를 바탕으로 확인해보자.

Dirver.java

```java
package com.ssonsh.refactoringstudy._08_shotgun_surgery._28_inline_function;

public class Driver {

    private int numberOfLateDeliveries;

    public Driver(int numberOfLateDeliveries) {
        this.numberOfLateDeliveries = numberOfLateDeliveries;
    }

    public int getNumberOfLateDeliveries() {
        return this.numberOfLateDeliveries;
    }
}
```

Rating.java

```java
package com.ssonsh.refactoringstudy._08_shotgun_surgery._28_inline_function;

public class Rating {

    public int rating(Driver driver) {
        return moreThanFiveLateDeliveries(driver) ? 2 : 1;
    }

    private boolean moreThanFiveLateDeliveries(Driver driver) {
        return driver.getNumberOfLateDeliveries() > 5;
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._28_inline_function;

import com.ssonsh.refactoringstudy._08_shotgun_surgery._28_inline_function.Driver;
import com.ssonsh.refactoringstudy._08_shotgun_surgery._28_inline_function.Rating;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RatingTest {

    @Test
    void rating() {
        Rating rating = new Rating();
        assertEquals(2, rating.rating(new Driver(15)));
        assertEquals(1, rating.rating(new Driver(3)));
    }

}
```

---

Rating 클래스의 **moreThanFiveLateDeliveries** 메서드를 확인해보자.

- 이런 메서드가 인다이렉션, 우회하는 메서드라고 할 수 있다.
- 메서드 이름으로 의미를 부여하긴 하였다.
    - 그러나 메서드 이름 대신에 코드를 그대로 보더라도?
    - 맥락을 파악하는데 있어서 크게 다르지가 않고 문제가 없다.
- 이런 경우 인라인으로 처리할 수 있는 것이다.

```java
package com.ssonsh.refactoringstudy._08_shotgun_surgery._28_inline_function;

public class Rating {

    public int rating(Driver driver) {
        return driver.getNumberOfLateDeliveries() > 5 ? 2 : 1;
    }

}
```

어떤가? 이정도의 메서드 Body 내용이라면? 맥락파악에 어려움이 없다.

오히려 메소드이름을 바탕으로 해석하는 것 자체가 더 어려운 일 일 수 있다.