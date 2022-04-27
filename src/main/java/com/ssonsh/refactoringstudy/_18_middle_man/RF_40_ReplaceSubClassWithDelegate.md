# 리팩토링 40. 서브클래스를 위임으로 바꾸기

**냄새** : 중재자

→ **“서브 클래스를 위임으로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**서브 클래스를 위임으로 바꾸기 Replace Subclass with Delegate**

- 어떤 객체의 행동이 카테고리에 따라 바뀐다면 보통 상속을 사용해서 **일반적인 로직은 슈퍼클래스**에 두고 
  - **특이한 케이스에 해당하는 로직을 서브클래스**를 사용해 표현한다.
  - 일반화와 특수화
- 하지만, 대부분 프로그래밍 언어에서 **`상속은 오직 한번만 사용할 수 있다.`**
    - 만약 어떤 객체를 두 가지 이상의 카테고리로 구분해야 한다면?
        - 상속을 두 번 활용할 순 없다..!
            - 이런 것들을 필드로 선언하는 위임방식으로 진행하면?
            - 얼마든지 확장할 수 있다.
        - 위임을 사용하게 되면!? 얼마든지 여러 다른 분류로 위임할 수 있다.
            - 다른 객체로 위임
    - ***“위임을 사용하면 얼마든지 여러가지 이유로 여러 다른 객체로 위임할 수 있다.”***
- 슈퍼 클래스가 바뀌면 모든 클래스에 영향을 줄 수 있다. 따라서 슈퍼클래스를 변경할 때 서브클래스까지 신경써야 한다.
    - 만약 서브클래스가 전혀 다른 모듈에 있다면?
    - **위임을 사용한다면 중간에 `인터페이스`를 만들어 의존성을 줄일 수 있다.**
        - 루즐리 커플드 라고 말한다. 느슨~하게!
- **`“상속 대신 위임을 선호하라”`는 결코 “상속은 나쁘다”는 말이 아니다.**
    - 처음엔 상속을 적용하고 언제든지 리팩토링 기술을 이용해 위임으로 전환할 수 있다.

    
    💡 서브클래스를 만들게 되는 이유는
    슈퍼클래스의 여러가지 타입이 있을 때 분류를 나눌 때 서브클래스로 나눌 수 있다.
    일반적인 로직이 있는데, 특정한 케이스에만 조금 다르게 행동하는 로직이 있을 수 있다.

---

아래 코드를 보면서 살펴보자.

- Booking은 일반적인 Booking이 있고 조금 특별한 PremiumBooking이 있다.
- Booking은 Talkback 을 하기 위해선 PeakDay가 아니어야 할 수 있다.

    ```java
    public boolean hasTalkback() {
        return this.show.hasOwnProperty("talkback") && !this.isPeakDay();
    }
    ```

- 또한 기본 요금 또한 다르다. (Booking과 PremiumBooking)

Booking.java

```java
package com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class Booking {

    protected Show show;

    protected LocalDateTime time;

    public Booking(Show show, LocalDateTime time) {
        this.show = show;
        this.time = time;
    }

    public boolean hasTalkback() {
        return this.show.hasOwnProperty("talkback") && !this.isPeakDay();
    }

    protected boolean isPeakDay() {
        DayOfWeek dayOfWeek = this.time.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    public double basePrice() {
        double result = this.show.getPrice();
        if (this.isPeakDay()) result += Math.round(result * 0.15);
        return result;
    }

}
```

PremiumBooking.java

```java
package com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate;

import java.time.LocalDateTime;

public class PremiumBooking extends Booking {

    private PremiumExtra extra;

    public PremiumBooking(Show show, LocalDateTime time, PremiumExtra extra) {
        super(show, time);
        this.extra = extra;
    }

    @Override
    public boolean hasTalkback() {
        return this.show.hasOwnProperty("talkback");
    }

    @Override
    public double basePrice() {
        return Math.round(super.basePrice() + this.extra.getPremiumFee());
    }

    public boolean hasDinner() {
        return this.extra.hasOwnProperty("dinner") && !this.isPeakDay();
    }
}
```

PremiumExtra.java

```java
package com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate;

import java.util.List;

public class PremiumExtra {

    private List<String> properties;

    private double premiumFee;

    public PremiumExtra(List<String> properties, double premiumFee) {
        this.properties = properties;
        this.premiumFee = premiumFee;
    }

    public double getPremiumFee() {
        return premiumFee;
    }

    public boolean hasOwnProperty(String property) {
        return this.properties.contains(property);
    }
}
```

Show.java

```java
package com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate;

import java.util.List;

public class Show {

    private List<String> properties;

    private double price;

    public Show(List<String> properties, double price) {
        this.properties = properties;
        this.price = price;
    }

    public boolean hasOwnProperty(String property) {
        return this.properties.contains(property);
    }

    public double getPrice() {
        return price;
    }
}
```

Test.java

```java
package com.ssonsh.refactoringstudy._40_replace_subclass_with_delegate;

import com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate.Booking;
import com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate.PremiumBooking;
import com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate.PremiumExtra;
import com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate.Show;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void basePrice() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);

        Booking booking = new Booking(lionKing, weekday);
        assertEquals(120, booking.basePrice());

        Booking premium = new PremiumBooking(lionKing, weekday, new PremiumExtra(List.of(), 50));
        assertEquals(170, premium.basePrice());
    }

    @Test
    void basePrice_on_peakDay() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);

        Booking booking = new Booking(lionKing, weekend);
        assertEquals(138, booking.basePrice());

        Booking premium = new PremiumBooking(lionKing, weekend, new PremiumExtra(List.of(), 50));
        assertEquals(188, premium.basePrice());
    }

    @Test
    void talkback() {
        Show lionKing = new Show(List.of(), 120);
        Show aladin = new Show(List.of("talkback"), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);

        assertFalse(new Booking(lionKing, weekday).hasTalkback());
        assertTrue(new Booking(aladin, weekday).hasTalkback());
        assertFalse(new Booking(aladin, weekend).hasTalkback());

        PremiumExtra premiumExtra = new PremiumExtra(List.of(), 50);
        assertTrue(new PremiumBooking(aladin, weekend, premiumExtra).hasTalkback());
        assertFalse(new PremiumBooking(lionKing, weekend, premiumExtra).hasTalkback());
    }

    @Test
    void hasDinner() {
        Show lionKing = new Show(List.of(), 120);
        LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
        LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);
        PremiumExtra premiumExtra = new PremiumExtra(List.of("dinner"), 50);

        assertTrue(new PremiumBooking(lionKing, weekday, premiumExtra).hasDinner());
        assertFalse(new PremiumBooking(lionKing, weekend, premiumExtra).hasDinner());
    }

}
```

---

현재 위 구조는 적절한 상속 구조를 사용한 예로 볼 수 있다.

그러나 PreminumBooking 이 시간이 지남에 따라 다양한 상속을 다양한 형태의 Booking이 발생된다거나 다른 상속 구조로 변화되어야 한다면 달라져야 한다.

현재 이 상속 구조를 위임으로 바꾼다면?

- 하위 클래스를 없애는 리팩토링이다.
- PreminumBooking을 없애는 것이다.
- 그러기 위해선 델리게이션 하기 위한 클래스가 필요하다.

PremiumDelegate.java

```java
package com.ssonsh.refactoringstudy._18_middle_man._40_replace_subclass_with_delegate;

public class PremiumDelegate {
    private Booking host;
    private PremiumExtra extra;

    public PremiumDelegate(Booking host, PremiumExtra extra) {
        this.host = host;
        this.extra = extra;
    }
}
```

지금까지 Booking을 생성하기 위해서 생성자를 사용했었는데

static한 클래스를 사용할 것이다. (팩토리 메서드 사용)

    💡 우선, 생성자와 팩토리 메서드는 상당히 큰 차이가 있다.

    **메서드 이름을 우리가 조금 더 자유롭게 사용할 수 있다.**
    - 어떤 클래스 생성자 이름은 항상 클래스 이름과 같아야 한다.
    - 즉 다양하게 표현할 수가 없다.

    **리턴해주는 객체의 타입이 조금 더 자유롭다**
    - Booking에서는 Booking을 리턴하고
    - PremiumBooking 에서는 PremiumBooking을 리턴하고


Booking.java

```java
protected static Booking createBook(Show show, LocalDateTime time){
    return new Booking(show, time);
}
protected static Booking createPremiumBooking(Show show, LocalDateTime time, PremiumExtra extra){
    PremiumBooking booking = new PremiumBooking(show, time, extra);
    booking.premiumDelegate = new PremiumDelegate(booking, extra);
    return booking;
}
```


    💡 현재는 상속 구조를 가진 PremiumBooking을 사용하고 있지만
    위에서 만든 Delegate를 이용하여 변환해 나갈 것이다.


위와 같이 팩토리 메서드를 이용하여 Booking, PremiumBooking을 생성하도록 변경한 후 Test코드에서도 직접 생성자로 인스턴스를 생성하던 코드를

팩토리 메서드를 이용하여 생성하도록 테스트 해본다.

```java
assertFalse(Booking.createBooking(noTalkbackShow, nonPeekDay).hasTalkback());
assertTrue(Booking.createBooking(talkbackShow, nonPeekDay).hasTalkback());
assertFalse(Booking.createBooking(talkbackShow, peekday).hasTalkback());

PremiumExtra premiumExtra = new PremiumExtra(List.of(), 50);
assertTrue(Booking.createPremiumBooking(talkbackShow, peekday, premiumExtra).hasTalkback());
assertFalse(Booking.createPremiumBooking(noTalkbackShow, peekday, premiumExtra).hasTalkback());
```

PremiumBooking에 있던 hasTalback 메서드에 대해 **Delegate로 위임시키도록 한다.**

PremiumBooking.java

- 실제 하는일 자체가 없고 premiumDelegate로 위임하였다.

```java
@Override
public boolean hasTalkback() {
    return this.premiumDelegate.hasTalkback();
}
```

PremiumDelegate.java

- 실제 로직을 수행하는 역할을 수행하는 부분이 된다.

```java
public boolean hasTalkback() {
    return this.host.show.hasOwnProperty("talkback");
}
```

이렇게 되면 Booking의 시선에서 봤을 때

hasTalkback의 메소드를 아래와 같이 변경할 수 있다.

- PremiumDelegate가 Null이 아니면? PremiumDelegate의 hasTalkback을 수행하고
- 그게 아니라면 Booking에서 정의한 기존 hasTalkback 로직을 수행한다.

```java
public boolean hasTalkback() {
    return (this.premiumDelegate != null) ?
           this.premiumDelegate.hasTalkback() :
           this.show.hasOwnProperty("talkback") && !this.isPeakDay();
}
```

자연스럽게 PremiumBooking에 있는 hasTalkback 메서드는 제거될 수 있다.

다른 테스트 케이스를 기준으로 확인해보자.

basePrice()

```java
@Test
void basePrice() {
    Show lionKing = new Show(List.of(), 120);
    LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);

    Booking booking = Booking.createBooking(lionKing, weekday);
    assertEquals(120, booking.basePrice());

    Booking premium = Booking.createPremiumBooking(lionKing, weekday, new PremiumExtra(List.of(), 50));
    assertEquals(170, premium.basePrice());
}
```

basePrice 를 산출하는 Booking의 메소드를 아래와 같이 변경할 수 있다.

- delegate가 있다면 delegate로 basePrice 산출 로직을 위임시켜 전달받는다.

```java
public double basePrice() {
    double result = this.show.getPrice();
    if (this.isPeakDay()) result += Math.round(result * 0.15);
    return (this.premiumDelegate != null) ? this.premiumDelegate.extendBasePrice(result) : result;
}
```

자연스럽게 PreimumDelegate 에서는 Booking에서 호출하는 extendBasePrice 메서드를 구현해주고 실제 이 로직이 존재했던 PremiumBooking 의 basePrice 메서드는 제거될 수 있다.

```java
public double extendBasePrice(double result) {
    return Math.round(result + this.extra.getPremiumFee());
}

```

다른 테스트 케이스를 기준으로 확인해보자

hasDinner()

- 이 기능은 PremiumBooking에만 존재하던 로직이다. (Booking에선 X)

```java
@Test
void hasDinner() {
    Show lionKing = new Show(List.of(), 120);
    LocalDateTime weekday = LocalDateTime.of(2022, 1, 20, 19, 0);
    LocalDateTime weekend = LocalDateTime.of(2022, 1, 15, 19, 0);
    PremiumExtra premiumExtra = new PremiumExtra(List.of("dinner"), 50);

    assertTrue(Booking.createPremiumBooking(lionKing, weekday, premiumExtra).hasDinner());
    assertFalse(Booking.createPremiumBooking(lionKing, weekend, premiumExtra).hasDinner());
}
```

이 상황만 본다면 hasDinner 자체에 컴파일 에러가 발생할 것이다.

왜? Booking, PremiumBooking 모두 가지고 있던 기능이 아니라 PremiumBooking에만 특수하게 존재하던 기능이기 때문이다.

이 때는 PremiumBooking의 로직을 Booking으로 pull up 해주고 다음 리팩토링을 진행해나간다.

- 실제 이 hasDinner 로직은 Booking이 가지고 있는 것이 아니라
- PremiumDelegate가 가지고 있고 로직을 구현해야 한다.

```java

// in PremiumDelegate
public boolean hasDinner() {
    return this.extra.hasOwnProperty("dinner") && !this.host.isPeakDay();
}
```

Booking에서는 false를 반환하며 delegate가 있다면 delegate의 hasDinner를 호출하도록 할 수 있다.

```java
// in Booking
public boolean hasDinner(){
    return this.premiumDelegate != null && this.premiumDelegate.hasDinner();
}
```

최종적으로는 PremiumBooking을 제거해내고

Booking에서도 서브 클래스였던 PremiumBooking을 직접적으로 사용하지 않도록 변경할 수 있다.

```java
public static Booking createPremiumBooking(Show show, LocalDateTime time, PremiumExtra extra){
    // PremiumBooking booking = new PremiumBooking(show, time, extra);
    Booking booking = createBooking(show, time);
    booking.premiumDelegate = new PremiumDelegate(booking, extra);
    return booking;
}
```

핵심 중 하나는 이 중간 역할 중재자 역할을 수행하는 Delegate가 생성되는 것이고

이 Delegate가 어떤 중재역할을 하느냐에 따라서 실제 구현 로직의 위치가 변경된다.

실제 구현 로직의 위치가 변경됨에 따라 상속이 아닌 위임으로써 문제를 해결해나갈 수 있게 되는 것이다.