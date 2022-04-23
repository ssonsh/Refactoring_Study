# 리팩토링 18. 변수 쪼개기

**냄새** : 가변 데이터

→ **“변수 쪼개기”** 를 통해 리팩토링 할 수 있다.

**변수 쪼개기 Split Variable**

- 어떤 변수가 여러번 재할당 되어도 적절한 경우
    - 반복문에서 순회하는데 사용하는 변수 또는 인덱스
        - for loop 의 i 와 같은!
    - 값을 축적시키는데 사용하는 변수
        - StringBuilder와 같은!
- **`그 밖에 경우에 재할당 되는 변수가 있다면 해당 변수는 여러 용도로 사용되는 것이며 변수를 분리해야 더 이해하기 좋은 코드를 만들 수 있다.`**
    - 변수 하나 당 하나의 책임 (Responsibility)을 지도록 만든다.
    - 상수를 활용하자. (자바스크립트의 const, 자바의 final)


    🎈 한 변수가 여러번 할당된다면 이 변수가 이 역할을 하고 있는게 맞는것인가?! 
    고민해보자.


---

아래 코드를 기반으로 살펴보자.

**Rectangle.java**

- 높이와 너비를 파라미터로 받아서 넓이와 지름을 구하는 메소드 updateGeometry 가 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._18_split_variable;

public class Rectangle {

    private double perimeter;
    private double area;

    public void updateGeometry(double height, double width) {
        double temp = 2 * (height + width);
        System.out.println("Perimeter: " + temp);
        perimeter = temp;

        temp = height * width;
        System.out.println("Area: " + temp);
        area = temp;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public double getArea() {
        return area;
    }
}
```

**Test.java**

```java
package com.ssonsh.refactoringstudy._18_split_variable;

import com.ssonsh.refactoringstudy._06_mutable_data._18_split_variable.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RectangleTest {

    @Test
    void updateGeomerty() {
        Rectangle rectangle = new Rectangle();
        rectangle.updateGeometry(10, 5);
        assertEquals(50d,rectangle.getArea());
        assertEquals(30d, rectangle.getPerimeter());

        rectangle.updateGeometry(5, 5);
        assertEquals(25d,rectangle.getArea());
        assertEquals(20d, rectangle.getPerimeter());
    }

}
```

---

**리팩토링을 진행해보자.**

- updateGeometry 메소드 내부에서 “temp” 라는 변수가 여러번 할당 되는 것을 볼 수 있다.
    - 지름을 구하기 위해 사용이 되었다.
        - `double temp = 2 * (height + width);`
    - 넓비를 구하는데 사용이 되었다.
        - `temp = height * width;`



    🎈 이 temp는 본인의 역할을 위해 선언되고 사용되고 있는가?


**좀 더 명확히 이 변수가 하는 일을 정의하고 활용하도록 리팩토링 하였다.**

```java
public class Rectangle {

    private double perimeter;
    private double area;

    public void updateGeometry(double height, double width) {
        double perimeter = 2 * (height + width);
        System.out.println("Perimeter: " + perimeter);
        this.perimeter = perimeter;

        double area = height * width;
        System.out.println("Area: " + area);
        this.area = area;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public double getArea() {
        return area;
    }
}
```

---

---

아래 코드로 다시 살펴보자.

**Haggis.java**

```java
package com.ssonsh.refactoringstudy._06_mutable_data._18_split_variable;

public class Haggis {

    private double primaryForce;
    private double secondaryForce;
    private double mass;
    private int delay;

    public Haggis(double primaryForce, double secondaryForce, double mass, int delay) {
        this.primaryForce = primaryForce;
        this.secondaryForce = secondaryForce;
        this.mass = mass;
        this.delay = delay;
    }

    public double distanceTravelled(int time) {
        double result;
        double acc = primaryForce / mass;
        int primaryTime = Math.min(time, delay);
        result = 0.5 * acc * primaryTime * primaryTime;

        int secondaryTime = time - delay;
        if (secondaryTime > 0) {
            double primaryVelocity = acc * delay;
            acc = (primaryForce + secondaryForce) / mass;
            result += primaryVelocity * secondaryTime + 0.5 * acc * secondaryTime + secondaryTime;
        }

        return result;
    }
}
```

```java
package com.ssonsh.refactoringstudy._18_split_variable;

import com.ssonsh.refactoringstudy._06_mutable_data._18_split_variable.Haggis;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HaggisTest {

    @Test
    void distance() {
        Haggis haggis = new Haggis(10d, 20d, 10, 5);
        assertEquals(50d, haggis.distanceTravelled(10));
        assertEquals(125d, haggis.distanceTravelled(20));
    }

}
```

위 Haggis.java의 distanceTravelled 메소드를 보면 “acc” 라는 변수가 두 번 할당이 되었다.

`double acc = primaryForce / mass;`

`acc = (primaryForce + secondaryForce) / mass;`

또한 “result” 라는 변수가 두 번 할당이 되었다.

`result = 0.5 * acc * primaryTime * primaryTime;`

`result += primaryVelocity * secondaryTime + 0.5 * acc * secondaryTime + secondaryTime;`


    
    🎈 그러나, result 변수와 같은 경우는 
    Return할 변수이며 값을 축적시키기 위한 용도임으로 해당 리팩토링 기술이 무조건 적용해야 한다? 라고 볼 수 없다. !



```java
public double distanceTravelled(int time) {
    double result;
    double primaryAcceleration = primaryForce / mass;
    int primaryTime = Math.min(time, delay);
    result = 0.5 * primaryAcceleration * primaryTime * primaryTime;

    int secondaryTime = time - delay;
    if (secondaryTime > 0) {
        double primaryVelocity = primaryAcceleration * delay;
        double secondaryAcceleration = (primaryForce + secondaryForce) / mass;
        result += primaryVelocity * secondaryTime + 0.5 * secondaryAcceleration * secondaryTime + secondaryTime;
    }

    return result;
}
```


    🎈 추가로 final 키워드를 추가시켜 더이상 변경되지 않을 변수라는 것을 표현해주는 것도 좋은 방법이다. 🙂


```java
final double primaryAcceleration = primaryForce / mass;
final double primaryVelocity = primaryAcceleration * delay;
```

---

아래 코드로 살펴보자.

Order.java

```java
package com.ssonsh.refactoringstudy._06_mutable_data._18_split_variable;

public class Order {

    public double discount(double inputValue, int quantity) {
        if (inputValue > 50) inputValue = inputValue - 2;
        if (quantity > 100) inputValue = inputValue - 1;
        return inputValue;
    }
}
```

위 코드는 메서드 입력값으로 넘어온 매개변수를 내부에서 Return 값으로 재사용 되는 것이다.

- 두가지 의미로 사용된 매개변수임을 알 수 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._18_split_variable;

public class Order {

    public double discount(double inputValue, int quantity) {
        double result = inputValue;
        if (inputValue > 50) result -= 2;
        if (quantity > 100) result -= 1;
        return result;
    }
}
```

조금 더 명확한 의미로써 inputValue 변수와 result 변수를 사용하도록 유도하였다.

**변수가 하나 더 선언되는게 안좋은 것 아니야?**

그것 보다는 의미가 명확한 변수를 사용함으로써 코드 가독성을 높여보자.