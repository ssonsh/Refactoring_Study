# 리팩토링 20. 세터 제거하기

**냄새** : 가변 데이터

→ **“세터 제거하기”** 를 통해 리팩토링 할 수 있다.

**세터 제거하기 Remove Setting Method**

- 세터를 제공한다는 것은 해당 필드가 변경될 수 있다는 것을 뜻한다.
- **`객체 생성시 처음 설정된 값이 변경될 필요가 없다면?`**
    - 해당 값을 설정할 수 있는 생성자를 만들고 세터를 제거하여
    - 변경될 수 있는 가능성을 없애야 한다.

**보통 습관적으로 Getter, Setter를 만들게 된다.**

- Getter는 쿼리하는 부분임으로 무관하지만
- Setter는 값을 변경하는 것이기 때문에 다시 한번 고민해봐야 한다.

---

아래 코드를 보자

Person.java

```java
package com.ssonsh.refactoringstudy._06_mutable_data._20_remove_setting_method;

public class Person {

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
```

```java
class PersonTest {

    @Test
    void person() {
        Person person = new Person();
        person.setId(10);
        person.setName("keesun");
        assertEquals(10, person.getId());
        assertEquals("keesun", person.getName());
        person.setName("whiteship");
        assertEquals("whiteship", person.getName());
    }

}
```

Person 객체는 id와 name을 가진다.

- id는 할당된 이후 변경될 일이 없지만
- name의 경우 할당된 이후에도 변경될 수 있다

위와 같은 케이스를 기준으로 본다면 Person 객체에서 setId(int id) 메소드가 필요 없는 것이다.

- `**Person 객체를 생성할 때 id 필드 값을 생성자로 주입받도록 하였다.**`
```java
package com.ssonsh.refactoringstudy._06_mutable_data._20_remove_setting_method;

public class Person {

    private String name;

    private int id;

    public Person(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

}
```
```java
@Test
void person() {
    Person person = new Person(10);
    // person.setId(10);
    person.setName("keesun");
    assertEquals(10, person.getId());
    assertEquals("keesun", person.getName());
    person.setName("whiteship");
    assertEquals("whiteship", person.getName());
}
```