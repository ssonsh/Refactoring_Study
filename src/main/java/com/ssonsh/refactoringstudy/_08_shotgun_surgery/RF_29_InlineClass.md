**냄새** : 산탄총 수술

→ **“클래스 인라인”** 를 통해 리팩토링 할 수 있다.

**클래스 인라인 Inline Class**

- 클래스 전체의 필드와 메서드를 다른 클래스로 옮겨가는 것이다.
- “**클래스 추출하기(Extract Class)**”의 반대에 해당하는 리팩토링
    - [리팩토링 26. 클래스 추출하기](https://www.notion.so/26-f1266e2b6e3949ea8d04cafb8827b4b1)
- 리팩토링을 하는 중에 클래스의 책임을 옮기다보면 클래스의 존재 이유가 빈약해 지는 경우가 발생될 수 있다.
- 두개의 클래스를 여러 클래스로 나누는 리팩토링을 하는 경우
    - 우선 “**클래스 인라인**”을 적용해서 **두 클래스의 코드를 한 곳으로 모으고**
    - 그 다음에 “**클래스 추출하기**”를 적용해 **새롭게 분리하는 리팩토링**을 적용할 수 있다.

---

아래 코드를 바탕으로 확인해보자.

Shipment.java

```java
package com.ssonsh.refactoringstudy._08_shotgun_surgery._29_inline_class;

public class Shipment {

    private TrackingInformation trackingInformation;

    public Shipment(TrackingInformation trackingInformation) {
        this.trackingInformation = trackingInformation;
    }

    public TrackingInformation getTrackingInformation() {
        return trackingInformation;
    }

    public void setTrackingInformation(TrackingInformation trackingInformation) {
        this.trackingInformation = trackingInformation;
    }

    public String getTrackingInfo() {
        return this.trackingInformation.display();
    }
}
```

TrackingInformation.java

```java
package com.ssonsh.refactoringstudy._08_shotgun_surgery._29_inline_class;

public class TrackingInformation {

    private String shippingCompany;

    private String trackingNumber;

    public TrackingInformation(String shippingCompany, String trackingNumber) {
        this.shippingCompany = shippingCompany;
        this.trackingNumber = trackingNumber;
    }

    public String display() {
        return this.shippingCompany + ": " + this.trackingNumber;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._29_inline_class;

import com.ssonsh.refactoringstudy._08_shotgun_surgery._29_inline_class.Shipment;
import com.ssonsh.refactoringstudy._08_shotgun_surgery._29_inline_class.TrackingInformation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipmentTest {

    @Test
    void trackingInfo() {
        Shipment shipment = new Shipment(new TrackingInformation("UPS", "12345"));
        assertEquals("UPS: 12345", shipment.getTrackingInfo());
    }

}
```

---

[Shipment.java](http://Shipment.java) 클래스와 [TrackingInformation.java](http://TrackingInformation.java) 클래스로 나뉘어져 있는데

TrackingInformation 클래스의 내용을 Shipment 클래스로 이동시키고자 한다.

- 우선 Shipment 클래스는 TrackingInformation 필드 하나만을 가지고 있고, 그 자체로서 수행하는 기능이 없다고 볼 수 있다.
- 이 Shipment 클래스가 TrackingInformation 외 다른 Shipment와 관련한 작업을 하는 코드가 함께 있다면
- 클래스 인라인 리팩토링에 대해 다시한번 고려해볼 수 있겠지만
- 지금은 그렇지 않다.

Shipment.java

- 진행하는 단계에 방법에 차이가 있지만
- 필드 먼저 옮기고
- 그 필드를 주입받는 생성자를 생성한다.
- 이 후 필드와 관련한 메소드들을 옮기도록 한다.

```java
package com.ssonsh.refactoringstudy._08_shotgun_surgery._29_inline_class;

public class Shipment {

    private String shippingCompany;

    private String trackingNumber;

    public Shipment(String shippingCompany, String trackingNumber) {
        this.shippingCompany = shippingCompany;
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingInfo() {
        return this.shippingCompany + ": " + this.trackingNumber;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
}
```