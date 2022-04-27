# 리팩토링 39. 슈퍼클래스를 위임으로 바꾸기

**냄새** : 중재자

→ **“슈퍼 클래스를 위임으로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**슈퍼 클래스를 위임으로 바꾸기 Replace Superclass with Delegate**

- 객체지향에서 “**상속**”은 **기존의 기능을 재사용**하는 쉬우면서 강력한 방법이지만
  **`때로는 적절하지 않은 경우도 있다.`**
- 서브 클래스는 슈퍼 클래스의 모든 기능을 지원해야 한다.
    - Stack 이라는 자료구조를 만들 때 List를 상속 받는 것이 좋을까?
- 서브 클래스는 슈퍼 클래스 **자리를 대체**하더라도 잘 동작해야 하는 것이 원칙이다.
    - **`리스코프 치환 원칙`**
- 서브 클래스는 슈퍼 클래스의 변경에 취약하다.
    - **`즉 서브 클래스는 슈퍼클래스에 강력하게 의존한다.`**
- 그렇다면 상속을 사용하지 말아야 하는가?
    - 상속은 적절한 경우에 사용한다면 매우 쉽고 효율적인 방법이다.
    - 따라서, 우선 상속을 적용한 이후에, 적절하지 않다고 판단되면 그 때 이 리팩토링을 적용하자.


    💡 자바는 단 하나만 상속을 받을 수 있다.


---

아래 코드를 보면서 살펴보자.

CategoryItem.java

```java
package com.ssonsh.refactoringstudy._18_middle_man._39_replace_superclass_with_delegate;

import java.util.List;

public class CategoryItem {

    private Integer id;

    private String title;

    private List<String> tags;

    public CategoryItem(Integer id, String title, List<String> tags) {
        this.id = id;
        this.title = title;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean hasTag(String tag) {
        return this.tags.contains(tag);
    }
}
```

[Scroll.java](http://Scroll.java)

```java
package com.ssonsh.refactoringstudy._18_middle_man._39_replace_superclass_with_delegate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Scroll extends CategoryItem {

    private LocalDate dateLastCleaned;

    public Scroll(Integer id, String title, List<String> tags, LocalDate dateLastCleaned) {
        super(id, title, tags);
        this.dateLastCleaned = dateLastCleaned;
    }

    public long daysSinceLastCleaning(LocalDate targetDate) {
        return this.dateLastCleaned.until(targetDate, ChronoUnit.DAYS);
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._39_replace_superclass_with_delegate;

import com.ssonsh.refactoringstudy._18_middle_man._39_replace_superclass_with_delegate.Scroll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScrollTest {

    @Test
    void daysSinceLastCleaning() {
        Scroll scroll = new Scroll(1, "whiteship", null, LocalDate.of(2022, 1, 10));
        assertEquals(5, scroll.daysSinceLastCleaning(LocalDate.of(2022, 1, 15)));
    }

}
```

**`Scorll`** 이 CategoryItem을 상속을 받고 있다.

- 이 Scroll 은 어떤 특정한 Item이지 Category는 아닌 상황이다면
- CategoryItem을 상속받는게 맞지 않을 것 같다는 판단이 들게 된다.
- 이를 상속이 아니라 위임으로 변경하고 싶다면..?

위임으로 변경하기.

- 상위 타입을 extends 상속 하는 것이 아니라 (제거)
- 상위 타입을 필드로 선언한다.
- 객체 생성 시 상위 타입의 필드를 함께 생성해준다.
    - 상속을 하던 것과 동일하게 어차피 생성을 위해선 상위 클래스를 위한 필드를 다 매개변수로 받고 있기 때문에 어려운 일이 아니다.


```java
package com.ssonsh.refactoringstudy._18_middle_man._39_replace_superclass_with_delegate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Scroll {

    private LocalDate dateLastCleaned;

    private CategoryItem categoryItem;

    public Scroll(Integer id, String title, List<String> tags, LocalDate dateLastCleaned) {
        this.dateLastCleaned = dateLastCleaned;
        this.categoryItem = new CategoryItem(id, title, tags);
    }
    
    public long daysSinceLastCleaning(LocalDate targetDate) {
        return this.dateLastCleaned.until(targetDate, ChronoUnit.DAYS);
    }
}
```