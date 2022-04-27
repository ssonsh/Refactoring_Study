# 리팩토링 38. 중재자 제거하기

**냄새** : 중재자

→ **“중재자 제거하기”** 를 통해 리팩토링 할 수 있다.

**중재자 제거하기 Remove Middle Man**

- “위임 숨기기”의 반대에 해당하는 리팩토링
    - [리팩토링 37. 위임 숨기기](https://www.notion.so/37-a8c34214640c4a31a9dbd1c3fcade852)
- 필요한 캡슐화의 정도는 시간에 따라 그리고 상황에 따라 바뀔 수 있다.
- 캡슐화의 정도를 “중재자 제거하기”와 “위임 숨기기” 리팩토링을 통해 **조절**할 수 있다.

    
    💡 결국 함께하는 구성원들과 조정을 해나가야 한다.
    중요한 것은 여러 방법을 시도해볼 수 있는 리팩토링 기술을 갖추고 있느냐이다.

    이후 조절을 통해 경험을 쌓아나가자


- 위임하고 있는 객체를 클라이언트가 사용할 수 있도록 getter 를 제공하고
    - 클라이언트는 메시지 체인을 사용하도록 코드를 고친 뒤에 캡슐화에 사용했던 메소드를 제거한다.
- **Law of Demeter** 를 **지나치게 따르기 보다는 상황에 맞게 사용**하도록 하자.

      💡 디미터의 법칙(Law of Demeter) “가장 가까운 객체만 사용한다”
  

---

**아래 코드를 보면서 살펴보자.**

Person.java

```java
package com.ssonsh.refactoringstudy._18_middle_man._38_remove_middle_man;

public class Person {

    private Department department;

    private String name;

    public Person(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Person getManager() {
        return this.department.getManager();
    }
}
```

Department.java

```java
package com.ssonsh.refactoringstudy._18_middle_man._38_remove_middle_man;

public class Department {

    private Person manager;

    public Department(Person manager) {
        this.manager = manager;
    }

    public Person getManager() {
        return manager;
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._38_remove_middle_man;

import com.ssonsh.refactoringstudy._18_middle_man._38_remove_middle_man.Department;
import com.ssonsh.refactoringstudy._18_middle_man._38_remove_middle_man.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    @Test
    void getManager() {
        Person nick = new Person("nick", null);
        Person keesun = new Person("keesun", new Department(nick));
        assertEquals(nick, keesun.getManager());
    }

}
```

테스트코드를 보면 “keesun” 이라는 Person 인스턴스가 Manager 인지 확인하기 위해

- getManager() 메서드를 호출하고 있다.

Person의 getManager() 메서드는 아래와 같다.


    💡 이 부분이 delegator 인것이다.


```java
public Person getManager() {
    return this.department.getManager();
}
```

- 자신의 필드인 department 인스턴스를 통해 manager 정보를 조회해온다.
- 즉 메시지 채인을 통해 (person → department) 정보를 알아내고 있다.

*여기서 Department 에 다양한 상태들이 있다면? 항상 Person을 거쳐서 그 정보를 가져오게 할 것인가..??*

Person 에서 Department를 조회할 수 있도록 getter를 열어주고

```java
public Department getDepartment(){
    return this.department;
}
```

Manager를 가져오는 부분을

```java
assertEquals(nick, keesun.getDepartment().getManager());
```

- 이와 같이 가져와서 처리할 수 있도록 할 수 있다.

알 수 있듯이 메시지 채인과 반대되는 개념이다.

메시지 채인으로 진행하는 것이 맞느냐, 중재자를 제거하는 것이 맞느냐?

중간의 균형을 맞춰야 한다.