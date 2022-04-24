# 리팩토링 26. 클래스 추출하기

**냄새** : 뒤엉킨 변경

→ **“클래스 추출하기”** 를 통해 리팩토링 할 수 있다.

**클래스 추출하기 Extract Class**

- 클래스가 다루는 **책임 (Responsibility)**이 많아질수록 클래스가 점차 커진다.
- 클래스를 쪼개는 기준
    - 데이터나 메소드 중 **일부가 매우 밀접한 관련**이 있는 경우
    - 일부 데이터가 **대부분 같이 바뀌는 경우**
    - 데이터 또는 메소드 중 일부를 삭제한다면 어떻게 될 것인가?
- **하위 클래스**를 만들어 책임을 분산시킬 수도 있다.

---

아래 코드를 보면서 파악해보자.

**Person.java**

```java
package com.ssonsh.refactoringstudy._07_divergent_change._26_extract_class;

public class Person {

    private String name;

    private String officeAreaCode;

    private String officeNumber;

    public String telephoneNumber() {
        return this.officeAreaCode + " " + this.officeNumber;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;4
    }

    public String officeAreaCode() {
        return officeAreaCode;
    }

    public void setOfficeAreaCode(String officeAreaCode) {
        this.officeAreaCode = officeAreaCode;
    }

    public String officeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
}
```

현재 Person 클래스를 구성하고 있는 필드는 name, officeAreaCode, officeNumber 3개이다.

여기서 officeAreaCode, officeNumber 두개의 필드는 사무실 연락처를 표현하기 위한 정보임을 유추할 수 있을 것이다.

- 즉 여기서, Person 과 이 사무실 연락처를 구성하는 두개 필드를 분리해낼 수 있을 것이다.

만약 두개 필드를 분리하기 위해 Extract Class 한다면 어떻게 될 까?

- 진행해보면 바로 알 수 있는 부분이다.

예상하기로는 telephonNumber() 메서드에서 return 하고 있는 구문에서 컴파일 에러가 발생할 것이다.

- 이 메소드 내부를 봤더니 office와 관련한 두개의 필드를 이용하여 결과를 산출하고 있다.
- 즉, 이 메소드 또한 Extract 한 사무실 연락처 Class 내부로 위임을 시켜버리면 될 것 같다.

TelephoneNumber.java

```java
package com.ssonsh.refactoringstudy._07_divergent_change._26_extract_class;

public class TelephoneNumber {
    private String officeAreaCode;
    private String officeNumber;

    public TelephoneNumber(String officeAreaCode, String officeNumber) {
        this.officeAreaCode = officeAreaCode;
        this.officeNumber = officeNumber;
    }

    public String telephoneNumber() {
        return this.officeAreaCode + " " + this.officeNumber;
    }

    public String getOfficeAreaCode() {
        return officeAreaCode;
    }
    public String getOfficeNumber() {
        return officeNumber;
    }

}
```

Person.java

```java
package com.ssonsh.refactoringstudy._07_divergent_change._26_extract_class;

public class Person {

    private String name;

    private TelephoneNumber telephoneNumber;

    public Person(String name, TelephoneNumber telephoneNumber) {
        this.name = name;
        this.telephoneNumber = telephoneNumber;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String telephoneNumber(){
        return this.telephoneNumber.telephoneNumber();
    }
}
```

---

여기서 TelephoneNumber 라는 클래스로 추출하였다.

그런데 Person 클래스 안에서는 officeAreaCode, officeNumber 와 같이 명확히 필드 명을 정의하고 사용할 수 있었는데

TelephoneNumber 클래스로 추출된 이후에

officeAreaCode, officeNumber 필드가 적절할까? 에 대해 고민도 해볼 수 있다.

오히려 TelephoneNumber 클래스에서는 office 라는 것이 붙는게 부자연스럽다.

```java
package com.ssonsh.refactoringstudy._07_divergent_change._26_extract_class;

public class TelephoneNumber {
    private String areaCode;
    private String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String telephoneNumber() {
        return this.areaCode + " " + this.number;
    }

    public String getAreaCode() {
        return areaCode;
    }
    public String getNumber() {
        return number;
    }

}
```

이후 Person 클래스에서 선언되어 있는 TelephoneNumber telephoneNumber 필드명을 바꿔줄 수 있을 것 같다.
```java
package com.ssonsh.refactoringstudy._07_divergent_change._26_extract_class;

public class Person {

    private String name;

    private TelephoneNumber officeTelephoneNumber;

    public Person(String name, TelephoneNumber officeTelephoneNumber) {
        this.name = name;
        this.officeTelephoneNumber = officeTelephoneNumber;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String telephoneNumber(){
        return this.officeTelephoneNumber.telephoneNumber();
    }
}
```
