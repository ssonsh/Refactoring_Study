# (*) 리팩토링 22. 여러 함수를 변환 함수로 묶기

**냄새** : 가변 데이터

→ **“여러 함수를 변환 함수로 묶기”** 를 통해 리팩토링 할 수 있다.

**여러 함수를 변환 함수로 묶기 Combine Functions into Transform**

- 관련 있는 여러 파생 변수를 만들어내는 함수가 여러 곳에서 만들어지고 사용된다면
    - 그러한 파생 변수를 “**`변환 함수(transform function)`**”을 통해 한 곳으로 모아둘 수 있다.
- **소스 데이터가 변경될 수 있는 경우**에는
    - “**`여러 함수를 클래스로 묶기(Combine Functions into Class)`**”를 사용하는 것이 적절하다.
- **소스 데이터가 변경되지 않는 경우**에는 두 가지 방법을 모두 사용할 수 있지만,
    - 변환 함수를 사용해서 불변 데이터의 필드로 생성해 두고 재사용할 수 있다.



🎈 기존 데이터를 입력으로 받아서 새로운 데이터로 만들어 내는 것을 transform function 변환함수라 한다.


---

아래 코드를 보면서 알아보자.

[Reading.java](http://Reading.java) 

- 이 Reading Record는 한번 만들어진 이후에 참조만 가능하고
- 변경할 수 없는 불변하는 필드라고 볼 수 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public record Reading(String customer, double quantity, Month month, Year year) {
}
```

Client1, Client2, Client3

```java
package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public class Client1 {

    double baseCharge;

    public Client1(Reading reading) {
        this.baseCharge = baseRate(reading.month(), reading.year()) * reading.quantity();
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBaseCharge() {
        return baseCharge;
    }
}
```

```java
package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public class Client2 {

    private double base;
    private double taxableCharge;

    public Client2(Reading reading) {
        this.base = baseRate(reading.month(), reading.year()) * reading.quantity();
        this.taxableCharge = Math.max(0, this.base - taxThreshold(reading.year()));
    }

    private double taxThreshold(Year year) {
        return 5;
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBase() {
        return base;
    }

    public double getTaxableCharge() {
        return taxableCharge;
    }
}
```

```java
package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public class Client3 {

    private double basicChargeAmount;

    public Client3(Reading reading) {
        this.basicChargeAmount = calculateBaseCharge(reading);
    }

    private double calculateBaseCharge(Reading reading) {
        return baseRate(reading.month(), reading.year()) * reading.quantity();
    }

    private double baseRate(Month month, Year year) {
        return 10;
    }

    public double getBasicChargeAmount() {
        return basicChargeAmount;
    }
}
```

Client1, 2, 3 은 비슷한 작업을 하고 있지만 추가적인 작업들이 수행되고 있다.

- 모두 생성자를 통해 Reading 이라는 매개변수를 받아들이고 있고
    - 받아드린 Reading 정보를 바탕으로 각자 원하는 방법으로 calculate를 수행한다.
- 또한, baseRate, taxThreashold 라는 메소드가 존재한다.

새로운 클래스를 하나 만들어보자 ReadingClient 

- 중복적인 작업을 처리하는 메소드들을
- ReadingClient로 가져올 수 있다.

ReadingClient.java

```java
package com.ssonsh.refactoringstudy._06_mutable_data._22_combine_functions_into_transform;

import java.time.Month;
import java.time.Year;

public class ReadingClient {
    protected double taxThreshold(Year year){
        return 5;
    }

    protected double baseRate(Month month, Year year){
        return 10;
    }

    /**
     * 변하지 않는 Reading reading 매개변수를
     * 또다른 변하지 않는 Immutable 한 EnrichReading 으로 반환한다.
     * @param reading
     * @return
     */
    protected EnrichReading enrichReading(Reading reading){
        return new EnrichReading(reading, calculateBaseCharge(reading));
    }

    public double calculateBaseCharge(Reading reading) {
        return baseRate(reading.month(), reading.year()) * reading.quantity();
    }
}
```
