# 리팩토링 23. 참조를 값으로 바꾸기

**냄새** : 가변 데이터

→ **“참조를 값으로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**참조를 값으로 바꾸기 Change Reference to Value**

- 레퍼런스(Reference) 객체 vs 값(Value) 객체
    - [ValueObject (martinfowler.com)](https://martinfowler.com/bliki/ValueObject.html)
    - “Objects that are equals due to the value of their properties, in this case their x and y coordinates, are called value objects”
    - 값 객체는 객체가 가진 필드의 값으로 동일성을 확인한다.
    - 값 객체는 변하지 않는다.
    - 어떤 객체의 변경 내역을 다른 곳으로 전파시키고 싶다면 레퍼런스, 아니라면 값 객체를 사용한다.


    🎈 변하는 값을, 변하지 않는 Value Object로 바꾸는 리팩토링 기술이라고 볼 수 있다.
    - 변하는 값 : 레퍼런스 (Reference)
    - 변하지 않는 값 : 값 객체 (Value Object)


- 객체 변경 사항을 다른 코드에 전파 시키고 싶다? Reference 사용이 적절
- 이와 반대로 사이드 이팩트 없이 그 값 그 자체로 사용하고 싶다면 Value Object가 적절

---

**아래 코드를 확인해보자.**

- Person 은 officeTelephoneNumber를 필드로 가지고 있다.
    - 이는 Reference로 가지고 있는 것이다.
- officeTelephoneNumber의 객체는 TelephoneNumber 이며
    - 내부에서 set 메소드를 통해 number 값을 변경할 수 있다.

Person.java

```java
package com.ssonsh.refactoringstudy._06_mutable_data._23_change_reference_to_value;

public class Person {

    private TelephoneNumber officeTelephoneNumber;

    public String officeAreaCode() {
        return this.officeTelephoneNumber.areaCode();
    }

    public void officeAreaCode(String areaCode) {
        this.officeTelephoneNumber.areaCode(areaCode);
    }

    public String officeNumber() {
        return this.officeTelephoneNumber.number();
    }

    public void officeNumber(String number) {
        this.officeTelephoneNumber.number(number);
    }

}
```

TelephoneNumber.java

```java
package com.ssonsh.refactoringstudy._06_mutable_data._23_change_reference_to_value;

public class TelephoneNumber {

    private String areaCode;

    private String number;

    public String areaCode() {
        return areaCode;
    }

    public void areaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String number() {
        return number;
    }

    public void number(String number) {
        this.number = number;
    }
}
```

위 TelephoneNumber 객체를 값 객체로 만들어서 사용해보고자 한다.

- Setter 메소드를 제거해주고
- 생성자를 통해 내부 필드를 주입받는다.
- 또한 필드를 모두 final 키워드로 불변하도록 만들어준다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._23_change_reference_to_value;

public class TelephoneNumber {

    private final String areaCode;

    private final String number;

    public TelephoneNumber(String areaCode, String number) {
        this.areaCode = areaCode;
        this.number = number;
    }

    public String areaCode() {
        return areaCode;
    }
    public String number() {
        return number;
    }
}
```

**`추가적으로 값 객체의 동등성을 비교할 때`**는 내부 필드 값들이 동일한지 여부에 따라 동등성을 비교해야 함으로 Equals And HashCode 메소드를 Overriding 해준다.

- TelephoneNumber 객체의 필드들을 통해 equals 를 처리하도록 재정의 하는 것이다.

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TelephoneNumber that = (TelephoneNumber) o;
    return Objects.equals(areaCode, that.areaCode) && Objects.equals(number, that.number);
}

@Override
public int hashCode() {
    return Objects.hash(areaCode, number);
}
```

**`→ 동등성 비교 테스트`**

```java
class TelephoneNumberTest {

    @Test
    void testEquals(){
        TelephoneNumber number1 = new TelephoneNumber("123", "1234");
        TelephoneNumber number2 = new TelephoneNumber("123", "1234");

        assertEquals(number1, number2);
    }

}
```


    🎈 만약 equals, hashcode 를 값 객체의 필드를 통해 처리하지 않도록 구현하지 않았으면 결과는 다르게 나왔을 것이다. (reference 비교에 의해)

    🎈 equals 는 알겠는데, 왜 hashcode도 구현?

      해당 값 객체가 Collection에 들어가게 되는 경우 특히 hashcode 값을 검사하는 Collection에 들어가게 되는 경우 (Set, HashSet 등)

      값이 같으면 hashcode도 같아야 하고, 값이 다르면 hashcode도 달라야 하기 때문


위와 같이 TelephoneNumber 객체를 값 객체로 변경한 이후

이를 사용하는 Person 에서도 값 객체를 사용하도록 처리해준다.

- 값 객체를 사용하게 됨으로 변경사항이 있을 때 새로운 객체를 할당해주도록 한다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._23_change_reference_to_value;

public class Person {

    private TelephoneNumber officeTelephoneNumber;

    public String officeAreaCode() {
        return this.officeTelephoneNumber.areaCode();
    }

    public void officeAreaCode(String areaCode) {
        this.officeTelephoneNumber = new TelephoneNumber(areaCode, this.officeNumber());
    }

    public String officeNumber() {
        return this.officeTelephoneNumber.number();
    }

    public void officeNumber(String number) {
        this.officeTelephoneNumber = new TelephoneNumber(this.officeAreaCode(), number);
    }

}
```

---

위 방법은 자바 14버전 이전의 방법으로 구현하고 사용하는 값 객체라고 볼 수 있고

자바 14버전 이후에는 record로 TelephoneNumber로 만들 수 있다.

**RecordTelephoneNumber.java**
```java
public class RecordTelephoneNumber(String areaCode, String number) {
}
```
```java
class RecordTelephoneNumberTest {

    @Test
    void recordTest(){
        RecordTelephoneNumber number1 = new RecordTelephoneNumber("123", "1234");
        RecordTelephoneNumber number2 = new RecordTelephoneNumber("123", "1234");

        assertEquals(number1, number2);
    }
}
```
