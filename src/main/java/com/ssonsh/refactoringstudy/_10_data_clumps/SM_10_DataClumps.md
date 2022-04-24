# 냄새 10. 데이터 뭉치

### 데이터 뭉치 Data Clumps

- 항상 뭉쳐 다니는 데이터는 한 곳으로 모아두는 것이 좋다.
    - 여러 클래스에 존재하는 비슷한 필드 목록
    - 여러 함수에 전달되는 매개변수 목록

<aside>
💡 어떤 클래스나 메서드에 여러개의 필드가 중복해서 나타날 때를 얘기한다.

여러 클래스에 동일한 목록의 필드들이 있거나, 여러 함수에 전달되는 매개변수에 비슷한 데이터들이 같이 항상 넘겨지는 경우

</aside>

→ 따로 빼내서 클래스로 만들거나 파라미터 목록을 줄이는 리팩토링 기술들을 활용할 수 있다.

**관련 리팩토링 기술**

- “**클래스 추출하기(Extract Class)**”를 사용해 여러 필드를 하나의 객체나 클래스로 모을 수 있다.
    - [리팩토링 26. 클래스 추출하기](https://www.notion.so/26-f1266e2b6e3949ea8d04cafb8827b4b1)
- “**매개변수 객체 만들기 (Introduce Parameter Object)**” 또는 “**객체 통째로 넘기기(Preserve Whole Object)**”를 사용해 메소드 매개변수를 개선할 수 있다.
    - [리팩토링 8. 매개변수 객체 만들기](https://www.notion.so/8-f7ded4bb1fc14b0ab8594b3ab5173083)
    - [리팩토링 9. 객체 통째로 넘기기](https://www.notion.so/9-7c5c56f2279e4fbcb6c7ddfff131cfd6)


---

Employee.java

```java
package com.ssonsh.refactoringstudy._10_data_clumps;

public class Employee {

    private String name;

    private String personalAreaCode;

    private String personalNumber;

    public Employee(String name, String personalAreaCode, String personalNumber) {
        this.name = name;
        this.personalAreaCode = personalAreaCode;
        this.personalNumber = personalNumber;
    }

    public String personalPhoneNumber() {
        return personalAreaCode + "-" + personalNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonalAreaCode() {
        return personalAreaCode;
    }

    public void setPersonalAreaCode(String personalAreaCode) {
        this.personalAreaCode = personalAreaCode;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }
}
```

Office.java

```java
package com.ssonsh.refactoringstudy._10_data_clumps;

public class Office {

    private String location;

    private String officeAreCode;

    private String officeNumber;

    public Office(String location, String officeAreCode, String officeNumber) {
        this.location = location;
        this.officeAreCode = officeAreCode;
        this.officeNumber = officeNumber;
    }

    public String officePhoneNumber() {
        return officeAreCode + "-" + officeNumber;
    }

    public String getOfficeAreCode() {
        return officeAreCode;
    }

    public void setOfficeAreCode(String officeAreCode) {
        this.officeAreCode = officeAreCode;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
}
```

Employee, Office 클래스의 필드 목록을 보자.

각각 전화번호를 만들기 위한 areaCode와 number 필드가 있는 것을 볼 수 있다.

```java
private String personalAreaCode;

private String personalNumber;
```

```java
private String officeAreCode;

private String officeNumber;
```

여기서 데이터 뭉치로 볼 수 있는 것이 바로 areaCode, number 라고 볼 수 있다.

이 둘은 항상 같이 다니게 된다. (현재 형상에서는 그렇다는 것이다.)

이 것을 클래스로 추출 해낼 수 있다.

TelephoneNumber.java

```java
package com.ssonsh.refactoringstudy._10_data_clumps;

public class TelephoneNumber {

    private String areaCode;
    private String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String findNumber(){
        return this.areaCode + "-" + this.number;
    }
}
```

그리고 이것을 사용할 수 있도록 할 수 있다.
```java
package com.ssonsh.refactoringstudy._10_data_clumps;

public class Employee {

    private String name;
    private TelephoneNumber personalTelephoneNumber;

    public Employee(String name, TelephoneNumber personalTelephoneNumber) {
        this.name = name;
        this.personalTelephoneNumber = personalTelephoneNumber;
    }

    public String personalTelephoneNumber() {
        return personalTelephoneNumber.findNumber();
    }

    public String getName() {
        return name;
    }

}
```
```java
package com.ssonsh.refactoringstudy._10_data_clumps;

public class Office {

    private String location;

    private TelephoneNumber officeTelephoneNumber;

    public Office(String location, TelephoneNumber officeTelephoneNumber) {
        this.location = location;
        this.officeTelephoneNumber = officeTelephoneNumber;
    }

    public String officeTelephoneNumber() {
        return this.officeTelephoneNumber.findNumber();
    }
}
```