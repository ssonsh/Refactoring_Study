# 상속 포기

### 냄새 23. 상속 포기 Refused Bequest

서브 클래스가 슈퍼클래스에서 제공하는 메서드나 데이터를 잘 활용하지 않는다는 것은 해당 상속구조에 문제가 있다는 것이다.

- 자식이 부모의 유산을 활용하지 않고 포기한다는 것이다.
- 기존의 서브클래스 또는 새로운 서브클래스를 만들고 슈퍼클래스에서
  “**메소드와 필드를 내려주면 (Push Down Method / Field)**” 슈퍼클래스에 공동으로 사용하는 기능만 남길 수 있다.

서브클래스가 슈퍼클래스의 기능을 **재사용하고 싶지만 인터페이스를 따르고 싶지 않은 경우**에는 “**슈퍼클래스 또는 서브클래스를 위임으로 교체하기**” 리팩토링을 적용할 수 있다.

- [리팩토링 39. 슈퍼클래스를 위임으로 바꾸기](https://www.notion.so/39-301dff33f11e4ed394a3f1706d1dabc4)
- [리팩토링 40. 서브클래스를 위임으로 바꾸기](https://www.notion.so/40-b3492d5697b34a3c966fa6e2be7097bd)

슈퍼클래스가 제공하는 어떤 메서드를 활용하거나 overriding 하려면 슈퍼클래스가 가지고 있는 규약을 따라야 한다.

    💡 서브클래스가 가지고 있어야할 구체적인 필드나 메서드들을 
    슈퍼클래스가 가지고 있게 됨으로써 이와 같은 냄새가 발생될 수 있다.
    - 이런 경유 구체적인 필드나 메서드들을 적절한 서브클래스로 Pull Down Method/Field


---

Employee 슈퍼클래스가 있고 이를 상속한 서브클래스인 Engineer, Salesman 서브클래스가 있다.

```java
package com.ssonsh.refactoringstudy._23_refused_bequest;

public class Employee {

    protected Quota quota;

    protected Quota getQuota() {
        return new Quota();
    }

}
```

```java
package com.ssonsh.refactoringstudy._23_refused_bequest;

public class Engineer extends Employee {

}
```

```java
package com.ssonsh.refactoringstudy._23_refused_bequest;

public class Salesman extends Employee {

}
```

Employee 슈퍼클래스에는 Quota 라는 클래스를 필드로 가지고 있고, Quota를 처리하기 위한 메소드도 존재한다.

- 여기서 이 Quota는 서브클래스인 Salesman 에 특화된 필드와 메소드이다.
- 즉, EMployee 슈퍼클래스를 상속한 2개 서브 클래스 중 하나의 서브클래스에만 활용된다는 것이다.

이런 경우 슈퍼클래스에 있는 구체적인 용도로 사용되는 필드와 메서드를 Pull Down Method/Field 를 이용하여 리팩토링 할 수 있따.

```java
package com.ssonsh.refactoringstudy._23_refused_bequest;

public class Employee {

}
```

```java
package com.ssonsh.refactoringstudy._23_refused_bequest;

public class Engineer extends Employee {

}
```

```java
package com.ssonsh.refactoringstudy._23_refused_bequest;

public class Salesman extends Employee {

    protected Quota quota;

    protected Quota getQuota() {
        return new Quota();
    }

}
```