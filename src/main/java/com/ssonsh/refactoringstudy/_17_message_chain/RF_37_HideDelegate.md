# 리팩토링 37. 위임 숨기기

**냄새** : 메시지 체인

→ **“위임 숨기기”** 를 통해 리팩토링 할 수 있다.

**위임 숨기기 Hide Delegate**

- 캡슐화(Encapsulation)란 어떤 모듈이 시스템의 다른 모듈을 최소한으로 알아야 한다는 것이다.
    - 그래야 어떤 모듈을 변경할 때 최소한의 모듈만 그 변경에 영향을 받을 것이고, 그래야 무언가 변경하기 쉽다.
- 처음 객체지향에서 캡슐화를 배울 때 필드를 메소드로 숨기는 것이라 배우지만,
  메소드 호출도 숨길 수 있다.

```java
// as-is
person.department().manager();

// to-be
prerson.getManager()
```

- **as-is** 코드의 경우 Department를 통해 Manager로 접근할 수 있다는 정보를 알아야지만
- **to-be** 코드와 같이 getManager()를 통해 위임을 숨긴다면,
  *클라이언트는 person의 getManager()만 알아도 된다.*
    - 나중에 getManager() 내부 구현이 바뀌어도 getManager()를 사용하는 클라이언트 코드는 바꾸지 않아도 된다는 것이다.


---

아래 코드를 보자

Person.java

```java
package com.ssonsh.refactoringstudy._17_message_chain._37_hide_delegate;

public class Person {

    private String name;
    private Department department;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
```

Department.java

```java
package com.ssonsh.refactoringstudy._17_message_chain._37_hide_delegate;

public class Department {

    private String chargeCode;
    private Person manager;

    public Department(String chargeCode, Person manager) {
        this.chargeCode = chargeCode;
        this.manager = manager;
    }

    public String getChargeCode() {
        return chargeCode;
    }

    public Person getManager() {
        return manager;
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._37_hide_delegate;

import com.ssonsh.refactoringstudy._17_message_chain._37_hide_delegate.Department;
import com.ssonsh.refactoringstudy._17_message_chain._37_hide_delegate.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    @Test
    void manager() {
        Person keesun = new Person("keesun");
        Person nick = new Person("nick");
        keesun.setDepartment(new Department("m365deploy", nick));

        Person manager = keesun.getDepartment().getManager();
        assertEquals(nick, manager);
    }

}
```

테스트 코드에서 볼 수 있듯이 Manager 정보를 알기 위해 person 객체로부터 체이닝이 시작된다.

```java
Person manager = keesun.getDepartment().getManager();
```

이 부분을 Extract Function 해내고

```java
Person manager = getManager(keesun);

private Person getManager(Person keesun) {
    return keesun.getDepartment().getManager();
}
```

Extract 한 getManager 메소드를 Move Function 하여 Person으로 이동시킨다.
```java
package com.ssonsh.refactoringstudy._17_message_chain._37_hide_delegate;

public class Person {

    private String name;

    private Department department;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Person getManager() {
        return getDepartment().getManager();
    }
}
```