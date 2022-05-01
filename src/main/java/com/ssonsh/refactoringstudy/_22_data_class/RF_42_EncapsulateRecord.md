# 리팩토링 42. 레코드 캡슐화하기

**냄새** : 데이터 클래스

→ **“레코드 캡슐화하기”** 를 통해 리팩토링 할 수 있다.

**레코드 캡슐화하기 (Encapsulate Record)**

- 변하는 데이터를 다룰 때 레코드 보다는 객체를 선호한다.
    - “**`레코드`**”란, **public 필드로 구성**된 데이터 클래스를 말한다.
    - 데이터를 메소드 뒤로 감추면 객체의 클라이언트는 어떤 데이터가 저장되어 있는지 신경쓸 필요가 없다.
    - 필드 이름을 변경할 때 점진적으로 변경할 수 있다.
    - 하지만 자바의 Record는 불변 객체라서 이런 리팩토링이 필요가 없다.
- **public 필드**를 사용하는 코드를 `private 필드`와 `Getter, Setter`를 사용하도록 변경한다.


    💡 [리팩토링 17. 변수 캡슐화 하기](https://www.notion.so/17-263cbc2d3c8749efaf6a60c35917d3f4) 리팩토링과 비슷한 개념이다.


---

아래 코드를 바탕으로 살펴보자.

Organization.java

- public 필드로만 구성된 객체이다.

```java
package com.ssonsh.refactoringstudy._22_data_class._42_encapsulate_record;

public class Organization {

    public String name;

    public String country;

}
```

이런 경우 (public 필드로만 구성) → 객체에 직접 접근을 할 수 있게 될 것이다.

- 메서드를 통해 접근하는 것이 아니라 .. !

메서드로 접근하나 바로 접근하나 똑같은거 아니야? 어차피 변경하려고 하는건 같은데?

- 다르다. 다름을 인지하자
- 메서드로 제공한다는 것은 이 필드들을 외부로부터 감출 수 있는 것이다. (캡슐화)
- 메서드와 필드가 1=1로 완벽히 일치하는게 아니다.
    - 필드를 변경하기 전 유효성 검증을 할 수도 있고
    - 로그를 쌓을수도 있는 것이다.


아래와 같이 private 필드로 변경하고 Getter, Setter 메소드를 통해 접근하도록 대응할 수 있따.

```java
package com.ssonsh.refactoringstudy._22_data_class._42_encapsulate_record;

public class Organization {

    private String name;

    private String country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
```

또한 자바에서 제공하는 record를 이용할 수 있다.

RecordOrganization.java

- 이 record는 이미 Encapsulate 되어 있기 때문에 값을 변경할 수 없다. (=불변)
- 즉, **`불변 객체`**인 경우 자바에서는 Record를 활용할 수 있을 것이다.
```java
package com.ssonsh.refactoringstudy._22_data_class._42_encapsulate_record;

public record RecordOrganization(String name, String country) {
}
```