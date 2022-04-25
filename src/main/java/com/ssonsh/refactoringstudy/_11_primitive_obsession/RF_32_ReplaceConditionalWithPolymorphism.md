# 리팩토링 32. 조건부 로직을 다형성으로 바꾸기

**냄새** : 기본형 집착

→ **“조건부 로직을 다형성으로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**조건부 로직을 다형성으로 바꾸기 Replace Conditional with Polymorphism**

- 복잡한 조건식을 상속과 다형성을 통해 보다 명확하게 분리할 수 있다.
- switch 문을 통해 타입에 따라 각기 다른 로직을 사용하는 코드에 적용할 수 있다.
- ***기본 동작과(타입에 따른) 특수한 기능이 섞여있는 경우 상속 구조를 만들어서 기본 동작을 상위클래스에 두고 특수한 기능을 하위클래스로 옮겨서 각 타입에 따른 “차이점”을 강조해낼 수 있다.***
- 모든 조건문을 다형성으로 옮겨야 하는가?
    - 단순한 조건문은 그대로 두어도 상관없다.
    - 오직 복잡한 조건문을 다형성을 활용해 좀 더 나은 코드를 만들 수 있는 경우에만 적용한다. (과용을 조심하자.)

여러 타입들이 있는 경우 문자열, 열거형 등으로 타입으로 구분하는데

타입에 따라 각기 다른 로직이 동작해야 한다면

상속구조로 바꾸고 로직을 하위 클래스로 옮겨 활용할 수 있다.

- switch, if 구문을 줄여낼 수 있다.

일반적인 로직이 있고, 여기에 파생된 특수한 로직이 들어가는 경우에도 적용할 수 있다.

- 같은 조건으로 a 케이스는 이렇게, b 케이스에서는 이렇게!

---

아래 코드를 보며 살펴보자.

Employee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.swtiches;

import java.util.List;

public class Employee {

    private String type;

    private List<String> availableProjects;

    public Employee(String type, List<String> availableProjects) {
        this.type = type;
        this.availableProjects = availableProjects;
    }

    public int vacationHours() {
        return switch (type) {
            case "full-time" -> 120;
            case "part-time" -> 80;
            case "temporal" -> 32;
            default -> 0;
        };
    }

    public boolean canAccessTo(String project) {
        return switch (type) {
            case "full-time" -> true;
            case "part-time", "temporal" -> this.availableProjects.contains(project);
            default -> false;
        };
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._32_replace_conditional_with_polymorphism.swtiches;

import com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.swtiches.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void fulltime() {
        Employee employee = new Employee("full-time", List.of("spring", "jpa"));
        assertEquals(120, employee.vacationHours());
        assertTrue(employee.canAccessTo("new project"));
        assertTrue(employee.canAccessTo("spring"));
    }

    @Test
    void partime() {
        Employee employee = new Employee("part-time", List.of("spring", "jpa"));
        assertEquals(80, employee.vacationHours());
        assertFalse(employee.canAccessTo("new project"));
        assertTrue(employee.canAccessTo("spring"));
    }

    @Test
    void temporal() {
        Employee employee = new Employee("temporal", List.of("jpa"));
        assertEquals(32, employee.vacationHours());
        assertFalse(employee.canAccessTo("new project"));
        assertFalse(employee.canAccessTo("spring"));
        assertTrue(employee.canAccessTo("jpa"));
    }

}
```

    💡 일반적으로 타입에 따라 로직이 달라지는 케이스이다.


위 Employee 내부를 살펴보면 switch 구문을 통해

직원의 타입을 구분하고 있고 (full-time, part-time, temporal) 이에 따라 로직이 구분되어 지는 것을 확인할 수 있다.

이 타입을 상속 구조로 만들어내보자.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/33f37b59-d6b1-4c0a-bb0f-e5a12f2e078b/Untitled.png)

Employee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.swtiches;

import java.util.List;

public abstract class Employee {

    protected List<String> availableProjects;

    public Employee(List<String> availableProjects) {
        this.availableProjects = availableProjects;
    }

    public Employee() {
    }

    public abstract int vacationHours();

    public boolean canAccessTo(String project){
        return this.availableProjects.contains(project);
    }
}
```

FullTimeEmployee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.swtiches;

public class FullTimeEmployee extends Employee{

    @Override
    public int vacationHours() {
        return 120;
    }

    @Override
    public boolean canAccessTo(String project) {
        return true;
    }
}
```

PartTimeEmployee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.swtiches;

import java.util.List;

public class PartTimeEmployee extends Employee{

    public PartTimeEmployee(List<String> availableProjects) {
        super(availableProjects);
    }

    @Override
    public int vacationHours() {
        return 80;
    }

}
```

TemporalEmployee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.swtiches;

import java.util.List;

public class TemporalEmployee extends Employee{

    public TemporalEmployee(List<String> availableProjects) {
        super(availableProjects);
    }

    @Override
    public int vacationHours() {
        return 32;
    }

}
```

---

또 다른 예로 살펴보자.

Voyage.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation;

public record Voyage(String zone, int length) {
}
```

VoyageHistory.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation;

public record VoyageHistory(String zone, int profit) {
}
```

VoyageRating.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation;

import java.util.List;

public class VoyageRating {

    private Voyage voyage;

    private List<VoyageHistory> history;

    public VoyageRating(Voyage voyage, List<VoyageHistory> history) {
        this.voyage = voyage;
        this.history = history;
    }

    public char value() {
        final int vpf = this.voyageProfitFactor();
        final int vr = this.voyageRisk();
        final int chr = this.captainHistoryRisk();
        return (vpf * 3 > (vr + chr * 2)) ? 'A' : 'B';
    }

    private int captainHistoryRisk() {
        int result = 1;
        if (this.history.size() < 5) result += 4;
        result += this.history.stream().filter(v -> v.profit() < 0).count();
        if (this.voyage.zone().equals("china") && this.hasChinaHistory()) result -= 2;
        return Math.max(result, 0);
    }

    private int voyageRisk() {
        int result = 1;
        if (this.voyage.length() > 4) result += 2;
        if (this.voyage.length() > 8) result += this.voyage.length() - 8;
        if (List.of("china", "east-indies").contains(this.voyage.zone())) result += 4;
        return Math.max(result, 0);
    }

    private int voyageProfitFactor() {
        int result = 2;

        if (this.voyage.zone().equals("china")) result += 1;
        if (this.voyage.zone().equals("east-indies")) result +=1 ;
        if (this.voyage.zone().equals("china") && this.hasChinaHistory()) {
            result += 3;
            if (this.history.size() > 10) result += 1;
            if (this.voyage.length() > 12) result += 1;
            if (this.voyage.length() > 18) result -= 1;
        } else {
            if (this.history.size() > 8) result +=1 ;
            if (this.voyage.length() > 14) result -= 1;
        }

        return result;
    }

    private boolean hasChinaHistory() {
        return this.history.stream().anyMatch(v -> v.zone().equals("china"));
    }

}
```

    💡 항해정보를 바탕으로 평가를 하는 로직이다.


[VoyageRating.java](http://VoyageRating.java) 로직을 살펴보면

아래와 같은 조건문이 반복되는 것을 살펴볼 수 있다.

`if (this.voyage.zone().equals("china") && this.hasChinaHistory())`

- 이 조건문이 동일한 조건문이지만, 어떤 Step 에서는 A 로직으로
- 또 다른 Step 에서는 B 로직으로의 수행이 진행된다.

즉, 위 조건문에 해당하는 것만 분리해낼 수 있다.

**[ChinaExperiancedVoyageRating.java](http://ChinaExperiancedVoyageRating.java) (extends VotageRating.class)**

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation;

import java.util.List;

public class ChinaExperiencedVoyageRating extends VoyageRating{
    public ChinaExperiencedVoyageRating(Voyage voyage,
                                        List<VoyageHistory> history) {
        super(voyage, history);
    }

    @Override
    protected int captainHistoryRisk() {
        int result = super.captainHistoryRisk() - 2;
        return Math.max(result, 0);
    }

    @Override
    protected int voyageProfitFactor() {
        return super.voyageProfitFactor() + 3;
    }

    @Override
    protected int voyageLengthFactor() {
        int result = 0;
        if (this.voyage.length() > 12) result += 1;
        if (this.voyage.length() > 18) result -= 1;
        return result;
    }

    @Override
    protected int historyLengthFactor() {
        return (this.history.size() > 10) ? 1 : 0;
    }
}
```

부모 클래스인 VotageRating 에서는 자식 클래스가 오버라이딩 할 수 있도록 접근 지시자를 private → protected로 변경해주었다.

특정 조건에 의해서 VotageRating (부모 클래스)를 활용할 것인지

ChinaExperiencedVoyageRating (자식 클래스)를 활용할 것인지 판단하기 위해 Factory 클래스를 활용한다.

**VoyageRatingFactory.java**

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation;

import java.util.List;

public class VoyageRatingFactory {
    public static VoyageRating createRating(Voyage voyage, List<VoyageHistory> history){

        if(voyage.zone().equals("china") && hasChinaHistory(history)){
            return new ChinaExperiencedVoyageRating(voyage, history);
        }else{
            return new VoyageRating(voyage, history);
        }
    }

    private static boolean hasChinaHistory(List<VoyageHistory> history) {
        return history.stream().anyMatch(v -> v.zone().equals("china"));
    }
}
```

Test.java
```java
package com.ssonsh.refactoringstudy._32_replace_conditional_with_polymorphism.variation;

import com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation.Voyage;
import com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation.VoyageHistory;
import com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation.VoyageRating;
import com.ssonsh.refactoringstudy._11_primitive_obsession._32_replace_conditional_with_polymorphism.variation.VoyageRatingFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VoyageRatingTest {

    @Test
    void westIndies() {
        VoyageRating voyageRating = VoyageRatingFactory.createRating(new Voyage("west-inides", 10),
                                                                     List.of(new VoyageHistory("east-indies", 5),
                                                                             new VoyageHistory("west-indies", 15),
                                                                             new VoyageHistory("china", -2),
                                                                             new VoyageHistory("west-africa", 7)));
        assertEquals('B', voyageRating.value());
    }

    @Test
    void china() {
        VoyageRating voyageRating = VoyageRatingFactory.createRating(new Voyage("china", 10),
                                                                     List.of(new VoyageHistory("east-indies", 5),
                                                                             new VoyageHistory("west-indies", 15),
                                                                             new VoyageHistory("china", -2),
                                                                             new VoyageHistory("west-africa", 7)));

        assertEquals('A', voyageRating.value());
    }

}
```