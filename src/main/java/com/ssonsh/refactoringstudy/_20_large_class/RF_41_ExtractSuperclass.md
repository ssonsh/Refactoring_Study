# 리팩토링 41. 슈퍼클래스 추출하기

**냄새** : 거대한 클래스

→ **“슈퍼클래스 추출하기”** 를 통해 리팩토링 할 수 있다.

**슈퍼클래스 추출하기 (Extract Superclass)**

- 두개의 클래스에서 비슷한 것들이 보인다면 “**`상속`**”을 적용하고
- 슈퍼클래스로 “**필드 올리기(Pull Up Field)**”와 “**메서드 올리기(Pull Up Method)**”를 사용한다.
    - [리팩토링 27. 필드 옮기기](https://www.notion.so/27-fd1b47cd660f458792776c5503c0c4ad)
    - [리팩토링 25. 함수 옮기기](https://www.notion.so/25-fcf63963c6c44abd8fb6bb48bb90325e)
- 대안으로 “**클래스 추출하기(Extract Class)**”를 적용해 위임을 사용할 수 있ㅎ다.
    - [리팩토링 26. 클래스 추출하기](https://www.notion.so/26-f1266e2b6e3949ea8d04cafb8827b4b1)
- 우선은 간단히 **`상속을 적용한 이후`**, 나중에 필요하다면
  “**슈퍼클래스를 위임으로 교체하기**”를 적용한다.
    - [리팩토링 39. 슈퍼클래스를 위임으로 바꾸기](https://www.notion.so/39-301dff33f11e4ed394a3f1706d1dabc4)
    - [리팩토링 40. 서브클래스를 위임으로 바꾸기](https://www.notion.so/40-b3492d5697b34a3c966fa6e2be7097bd)

---

**아래 코드를 기준으로 살펴보자**

- Employee의 경우 id, name, monthlyCoast 3개 필드가 존재한다.
- Department의 경우 여러 staff(Employees)로 구성되어 있고 이름이 존재한다.
- 여기서 각 Cost를 판단하는 메서드들이 존재한다.

Department.java

```java
package com.ssonsh.refactoringstudy._20_large_class._41_extract_superclass;

import java.util.List;

public class Department {

    private String name;

    private List<Employee> staff;

    public String getName() {
        return name;
    }

    public List<Employee> getStaff() {
        return staff;
    }

    public double totalMonthlyCost() {
        return this.staff.stream().mapToDouble(e -> e.getMonthlyCost()).sum();
    }

    public double totalAnnualCost() {
        return this.totalMonthlyCost() * 12;
    }

    public int headCount() {
        return this.staff.size();
    }
}
```

Employee.java

```java
package com.ssonsh.refactoringstudy._20_large_class._41_extract_superclass;

public class Employee {

    private Integer id;

    private String name;

    private double monthlyCost;

    public double annualCost() {
        return this.monthlyCost * 12;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMonthlyCost() {
        return monthlyCost;
    }
}
```

공통적으로 name, montlyCost, AnnualCost 가 존재한다.

→ Party 라는 상위 클래스를 만들어 기능들을 올려주자.

1. name 필드를 party 클래스로 올려준다.
2. montlyCost, AnnualCost 메서드를 올려주자.

Party.java

```java
package com.ssonsh.refactoringstudy._20_large_class._41_extract_superclass;

public abstract class Party {
    protected String name;

    public Party(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double annualCost() {
        return this.monthlyCost() * 12;
    }

    abstract protected double monthlyCost() ;

}
```

- 여기서 monthlyCost 메서드의 경우 Department, Employee 객체가 구현하는 방식이 다름으로
- abstract 추상 메서드로 변경해주었고 이를 상속받은 구체 클래스에서 직접 구현하도록 하였다.
- abstract 추상 메서드가 됨에 따라 Party 클래스도 자연스럽게 추상 클래스로 변경되었따.

각 Department와 Employee 클래스는 아래와 같이 변경될 수 있따.

Department.java

- Party를 extends 받으며
- monthlyCost 추상 메서드를 Overriding 하여 자체적인 구현 로직을 작성하였따.

```java
package com.ssonsh.refactoringstudy._20_large_class._41_extract_superclass;

import java.util.List;

public class Department extends Party{

    private List<Employee> staff;

    public Department(String name) {
        super(name);
    }

    public List<Employee> getStaff() {
        return staff;
    }

    @Override
    public double monthlyCost() {
        return this.staff.stream().mapToDouble(Employee::monthlyCost).sum();
    }

    public int headCount() {
        return this.staff.size();
    }
}
```

Employee.java

- Party 추상 클래스를 extends 하였으며
- monthlyCost 에 대한 메서드를 Overrinding 하여 Employee 자체의 로직으로 구현하였다.
```java
package com.ssonsh.refactoringstudy._20_large_class._41_extract_superclass;

public class Employee extends Party{

    private Integer id;

    private double monthlyCost;

    public Employee(String name) {
        super(name);
    }

    public Integer getId() {
        return id;
    }

    @Override
    public double monthlyCost() {
        return monthlyCost;
    }
}
```