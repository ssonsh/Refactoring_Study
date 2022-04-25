# 리팩토링 31. 타입 코드를 서브클래스로 바꾸기

**냄새** : 기본형 집착

→ **“타입 코드를 서브 클래스로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**타입 코드를 서브 클래스로 바꾸기 Replace Type Code with Subclasses**

- 비슷하지만 다른 것들을 표현해야 하는 경우, 문자열(String), 열거형(enum) 숫자(int) 등으로 표현하기도 한다.
    - 예) 주문 타입, “일반 주문”, “빠른 주문”
    - 예) 직원 타입, “엔지니어”, “매니저”, “쎄일즈”
- 타입을 서브클래스로 바꾸는 계기
    - 조건문을 다형성으로 표현할 수 있을 때, 서브클래스를 만들고
        - “**조건부 로직을 다형성으로 바꾸기**”를 적용한다.
    - 특정 타입에만 유효한 필드가 있을 떄 서브클래스를 만들고
      “**필드 내리기**”를 적용한다.


---

아래 코드를 보며 살펴보자.

**직접 이 클래스를 서브 클래스로 만들 수 있는 케이스**

(direct_inheritance) Employee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.direct_inheritance;

import java.util.List;

public class Employee {

    private String name;

    private String type;

    public Employee(String name, String type) {
        this.validate(type);
        this.name = name;
        this.type = type;
    }

    private void validate(String type) {
        List<String> legalTypes = List.of("engineer", "manager", "salesman");
        if (!legalTypes.contains(type)) {
            throw new IllegalArgumentException(type);
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
```

- 여기서 type 필드가 시간이 지날수록 더 많은 일들을 해야하고, 각 타입에 맞게 원하는 동작이 분리되어야 한다고 가정하자.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/573077b9-dc18-49f4-a114-2a519f2981fc/Untitled.png)

Employee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.direct_inheritance;

public abstract class Employee {

    private String name;

    protected Employee(String name) {
        this.name = name;
    }

    // type에 따른 서브클래스를 생성하여 반환하는 팩토리 메소드
    public static Employee createEmployee(String name, String type){
        return switch (type) {
            case "engineer" -> new Engineer(name);
            case "manager" -> new Manager(name);
            case "salesman" -> new Salesman(name);
            default -> throw new IllegalArgumentException(type);
        };
    }

    protected abstract String getType();

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", type='" + getType() + '\'' +
                '}';
    }
}
```

Engineer.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.direct_inheritance;

public class Engineer extends Employee{
    public Engineer(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "engineer";
    }
}
```

Manager.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.direct_inheritance;

public class Manager extends Employee{
    public Manager(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "manager";
    }
}
```

Salesman.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.direct_inheritance;

public class Salesman extends Employee{
    public Salesman(String name) {
        super(name);
    }

    @Override
    public String getType() {
        return "salesman";
    }
}
```

---

---

**이미 상속 구조가 있어서 간접적인 상속을 활용해야 하는 경우**

(indirect_inheritance) Employee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.indirect_inheritance;

import java.util.List;

public class Employee {

    private String name;

    private String type;

    public Employee(String name, String type) {
        this.validate(type);
        this.name = name;
        this.type = type;
    }

    private void validate(String type) {
        List<String> legalTypes = List.of("engineer", "manager", "salesman");
        if (!legalTypes.contains(type)) {
            throw new IllegalArgumentException(type);
        }
    }

    public String capitalizedType() {
        return this.type.substring(0, 1).toUpperCase() + this.type.substring(1).toLowerCase();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
```

(indirect_inheritance) FullTimeEmployee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.indirect_inheritance;

public class FullTimeEmployee extends Employee {
    public FullTimeEmployee(String name, String type) {
        super(name, type);
    }
}
```

(indirect_inheritance) PartTimeEmployee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.indirect_inheritance;

public class PartTimeEmployee extends Employee {
    public PartTimeEmployee(String name, String type) {
        super(name, type);
    }
}
```

<aside>
💡 이미 상속 구조가 있는 경우엔 
리팩토링하고자 하는 Primitive Type을 대체할 수 있는 객체를 생성해낸다.

</aside>

String type → EmployeeType type 으로 리팩토링 할 수 있다.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c3939c7d-47ad-4fe7-b345-9162977d843e/Untitled.png)

EmployeeType.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.indirect_inheritance;

public class EmployeeType {
    public String capitalizedType() {
        return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1).toLowerCase();
    }
}
```

Engineer.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.indirect_inheritance;

public class Engineer extends EmployeeType{

    @Override
    public String toString(){
        return "engineer";
    }
}
```

Manager.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.indirect_inheritance;

public class Manager extends EmployeeType {

    @Override
    public String toString() {
        return "manager";
    }
}
```

Salesman.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.indirect_inheritance;

public class Salesman  extends EmployeeType{

    @Override
    public String toString(){
        return "salesman";
    }
}
```

활용하는 Client 코드도 아래와 같이 변경될 수 있다.

Employee.java

```java
package com.ssonsh.refactoringstudy._11_primitive_obsession._31_replace_type_code_with_subclasses.indirect_inheritance;

public class Employee {

    private String name;

    private EmployeeType type;

    public Employee(String name, String typeValue) {
        this.name = name;
        this.type = this.employeeType(typeValue);
    }

    private EmployeeType employeeType(String typeValue) {
        return switch (typeValue) {
            case "engineer" -> new Engineer();
            case "manager" -> new Manager();
            case "salesman" -> new Salesman();
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    public String toString() {
        return "Employee{" +
               "name='" + name + '\'' +
               ", type='" + this.type.toString() + '\'' +
               '}';
    }
}
```

- Primitive Type을 사용하던 type 필드는 제거가 되었고
- 객체, 서브클래스 형태로 구성한 EmployeeType을 필드로 대체되었다.
- Employee 클래스를 생성하기 위해 넘어온 매개변수인 typeValue를 통해 자체적인 employeeType 이라는 메서드를 이용하여 EmployeeType 필드를 정의해낼 수 있다.