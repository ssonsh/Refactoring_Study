# 리팩토링 17. 여러 함수를 클래스로 묶기

**냄새** : 전역 데이터

→ **“변수 캡슐화 하기”** 를 통해 리팩토링 할 수 있다.

**변수 캡슐화 하기 Encapsulating Variables**

- 비슷한 매개변수 목록을 여러 함수에서 사용하고 있다면 **해당 메소드를 모아서 클래스**를 만들 수 있다.
- 데이터 구조를 변경하는 작업을 그보다는 조금 더 수월한 메소드 구조 변경 작업으로 대체할 수 있다.



    🎈 일반적으로 개발을 진행하면서 변경사항이 발생했을 때
    변수를 변경하는 것 보다 메소드를 변경하는 것이 더욱 쉽고 편리하다.

    변수를 변경하면 해당 변수를 사용하는 모든 곳을 찾아서 변경해줘야 하는 리스크가 있다.

    메소드 변경은 기존 메소드를 그대로 둔 상태에서 새로운 메소드를 만들고 새로 만든 메소드에서 새로운 코드를 작성한다.

    나머지 코드들은 그대로 남겨두거나 변경하거나 할 수 있는 것이다.
    즉, 점진적 변경이 가능하게 되는 것이다.



- 데이터가 사용되는 범위가 클수록 캡슐화를 하는 것이 중요해진다.
    - 함수를 사용해서 값을 변경하면 보다 쉽게 검증 로직을 추가하거나
    - 변경에 따르는 후속 작업을 추가하는 것이 편리하다.
- 불변 데이터의 경우 이런 리팩토링을 적용할 필요가 없다. (상수)

---

아래 코드를 기반으로 확인해보자.

  
    🎈 [Home.java](http://Home.java) 에서는 Thermostats 클래스의 전역 변수들 (static)을 사용하고 있는 것을 볼 수 있다.
    여기서 Thermostats 의 전역 변수들은 Home 클래스 뿐만 아니라 여러 클래스에서도 접근할 수 있고 수정할 수 있는 코드로 작성되어 있다.

**Home.java**

```java
package com.ssonsh.refactoringstudy._05_global_data._17_encapsulate_variable;

public class Home {

    public static void main(String[] args) {
        System.out.println(Thermostats.targetTemperature);
        Thermostats.targetTemperature = 68;
        Thermostats.readInFahrenheit = false;
    }
}
```

**Thermostats.java**

```java
package com.ssonsh.refactoringstudy._05_global_data._17_encapsulate_variable;

public class Thermostats {

    public static Integer targetTemperature = 70;

    public static Boolean heating = true;

    public static Boolean cooling = false;

    public static Boolean readInFahrenheit = true;

}
```

위 코드의 클라이언트 단인 Home 을 보자

`Thermostats.*targetTemperature* = 68;` 로 값을 설정하고 있지만,

정말 말도 안되게 -10111111, 99999 와 같은 값을 설정할 수 있게 된다.

- 왜? 그저 Integer 로 선언된 변수이기 때문이다.

의도하지 않게 값을 변경하게 될 수 있는 경우라면 변수를 직접적으로 수정할 수 있게 하지 않도록 하며, 메소드를 이용하여 수정할 수 있도록 할 수 있다.

- 또한 필요한 유효성 검증을 처리해낼 수도 있다.

가장 쉽게 할 수 있는 방법이 Getter, Setter 를 만들어서 사용하도록 하는 것이다.

아래와 같이 Thermostats 클래스의 변수들을 처리할 수 있는 Method 를 정의한다.

```java
package com.ssonsh.refactoringstudy._05_global_data._17_encapsulate_variable;

public class Thermostats {

    public static Integer targetTemperature = 70;
    public static Boolean heating = true;
    public static Boolean cooling = false;
    public static Boolean readInFahrenheit = true;

    public static Integer getTargetTemperature() {
        return targetTemperature;
    }

    public static void setTargetTemperature(Integer targetTemperature) {
        Thermostats.targetTemperature = targetTemperature;
    }

    public static Boolean getHeating() {
        return heating;
    }

    public static void setHeating(Boolean heating) {
        Thermostats.heating = heating;
    }

    public static Boolean getCooling() {
        return cooling;
    }

    public static void setCooling(Boolean cooling) {
        Thermostats.cooling = cooling;
    }

    public static Boolean getReadInFahrenheit() {
        return readInFahrenheit;
    }

    public static void setReadInFahrenheit(Boolean readInFahrenheit) {
        Thermostats.readInFahrenheit = readInFahrenheit;
    }
}
```

클라이언트에서는 변수를 직접적으로 처리하지 않고

활용할 수 있는 Method를 통해 접근한다.

```java
package com.ssonsh.refactoringstudy._05_global_data._17_encapsulate_variable;

public class Home {

    public static void main(String[] args) {
        System.out.println(Thermostats.getTargetTemperature());
        Thermostats.setTargetTemperature(68);
        Thermostats.setReadInFahrenheit(false);
    }
}
```

여기서 이어서 Thermostats 에서 변수를 직접적으로 접근하지 못하도록

접근 지시자를 바꿔준다. public → private

```java
private static Integer targetTemperature = 70;
private static Boolean heating = true;
private static Boolean cooling = false;
private static Boolean readInFahrenheit = true;
```

또한 메소드 내에서 원하는 유효성 검증을 처리해낼 수도 있다.
```java
public static void setTargetTemperature(Integer targetTemperature) {
    // TODO. targetTemperature 값은 -10 ~ 50 사이 값이어야 한다.
    // TODO. targetTemperature 값이 설정 가능한 범위를 벗어난다면? Notification
    
    Thermostats.targetTemperature = targetTemperature;
}
```