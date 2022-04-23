# 리팩토링 19. 질의 함수와 변경 함수 분리하기

**냄새** : 가변 데이터

→ **“질의 함수와 변경 함수 분리하기”** 를 통해 리팩토링 할 수 있다.

**질의 함수와 변경 함수 분리하기 Separate Query from Modifier**

- “눈에 띌만한” 사이드 이팩트 없이 값을 조회할 수 있는 메소드는 테스트 하기도 쉽고, 메소드를 이동하기 편하다.
- **`명령-조회 분리(command-query separation) 규칙`**
    - 어떤 값을 리턴하는 함수는 사이드 이팩트가 없어야 한다.
- **`“눈에 띌만한(observable) 사이드 이팩트”`**
    - 아주 분명한 값의 변경이 있는 것을 의미한다.
    - 가령, 캐시는 중요한 객체 상태 변화는 아니다.
    - 따라서 어떤 메서드 호출로 인해, 캐시 데이터를 변경하더라도 분리할 필요는 없다.


    🎈 지금까지 Query라는 단어가 나오는데, 여기서 Query는 어떤 내용을 조회하는 메소드라고 보면 된다.


    🎈 Getter와 Setter 처럼, 값을 조회하는 함수와 값을 변경하는 함수를 변경한다는 것이다. - Command/Query Separation

---

**아래 코드를 보면서 확인해보자.**

Billing.java

- 여기서 `getTotalOutstandingAndSendBill` 메소드를 보자
    - TotalOutstanding 총계를 산출하고
    - Bill 을 보내는 일을 같이한다.
    - 즉, 하나의 메소드에서 두가지 일을 하고 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier;

public class Billing {

    private Customer customer;

    private EmailGateway emailGateway;

    public Billing(Customer customer, EmailGateway emailGateway) {
        this.customer = customer;
        this.emailGateway = emailGateway;
    }

    public double getTotalOutstandingAndSendBill() {
        double result = customer.getInvoices().stream()
                .map(Invoice::getAmount)
                .reduce((double) 0, Double::sum);
        sendBill();
        return result;
    }

    private void sendBill() {
        emailGateway.send(formatBill(customer));
    }

    private String formatBill(Customer customer) {
        return "sending bill for " + customer.getName();
    }
}
```

```java
class BillingTest {

    @Test
    void totalOutstanding() {
        Billing billing = new Billing(new Customer("keesun", List.of(new Invoice(20), new Invoice(30))),
                                      new EmailGateway());
        assertEquals(50d, billing.getTotalOutstandingAndSendBill());
    }

}
```

getTotalOutstandingAndSendBill() 메소드를 분리하자.

- totalOutstanding을 반환하는 메소드와
- sendBill()을 수행하는 메소드

여기서 sendBill 메소드는 private로 이미 정의되어 있기 때문에 이를 사용할 수 있도록 public 접근지시자로 변경해주고

getTotalOutstandingAndSendBill() 메소드에서 sendBIll(); 부분을 제거해준다.

추가적으로 메소드명을 변경해준다.

```java
public double totalOutstanding() {
    return customer.getInvoices().stream()
                   .map(Invoice::getAmount)
                   .reduce((double) 0, Double::sum);
}

public void sendBill() {
    emailGateway.send(formatBill(customer));
}
```

이를 통해 사용하는 코드에서는 아래와 같이 변경될 수 있다.

```java
@Test
void totalOutstanding() {
    Billing billing = new Billing(new Customer("keesun", List.of(new Invoice(20), new Invoice(30))),
                                  new EmailGateway());

    double totalOutstanding = billing.totalOutstanding();

    Assertions.assertEquals(50d, billing.totalOutstanding());
}
```

---

아래 코드를 보면서 알아보자.

**Criminal.java**

- People 컬랙션을 순회하면서 “Don” 혹은 “John” 이라는 사람을 찾아서
- setOffAlarms() → 알람을 끄고
- 찾은 People의 name을 반환한다.


    🎈 이 메소드 또한 두가지 일을 하는 것을 알 수 있다.

```java
package com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier;

import java.util.List;

public class Criminal {

    public String alertForMiscreant(List<Person> people) {
        for (Person p : people) {
            if (p.getName().equals("Don")) {
                setOffAlarms();
                return "Don";
            }

            if (p.getName().equals("John")) {
                setOffAlarms();
                return "John";
            }
        }

        return "";
    }

    private void setOffAlarms() {
        System.out.println("set off alarm");
    }
}
```

```java
package com.ssonsh.refactoringstudy._19_separate_query_from_modifier;

import com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier.Criminal;
import com.ssonsh.refactoringstudy._06_mutable_data._19_separate_query_from_modifier.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CriminalTest {

    @Test
    void alertForMiscreant() {
        Criminal criminal = new Criminal();
        String found = criminal.alertForMiscreant(List.of(new Person("Keesun"), new Person("Don")));
        assertEquals("Don", found);

        found = criminal.alertForMiscreant(List.of(new Person("John"), new Person("Don")));
        assertEquals("John", found);
    }

}
```

조회하는 함수를 먼저 분리해낸다.

- 기존 함수에 있었던 사이드 이팩트 부분을 제거한다.

```java
public String findMiscreant(List<Person> people) {
    for (Person p : people) {
        if (p.getName().equals("Don")) {
            return "Don";
        }

        if (p.getName().equals("John")) {
            return "John";
        }
    }

    return "";
}
```

사이드이팩트(값을 변경하는 부분)이었던 메소드는 반환값이 없도록 만들고 값만 변경하는 작업을 하도록 변경해준다.

```java
public void alertForMiscreant(List<Person> people) {
    for (Person p : people) {
        if (p.getName().equals("Don")) {
            setOffAlarms();
        }

        if (p.getName().equals("John")) {
            setOffAlarms();
        }
    }
}
```


    🎈 이렇게 되면 하나의 메소드가 조회하는 부, 변경하는 부 메소드 각각 하나씩 분리가 되었다.


위와 같이 변경되었다면 사이드 이팩트가 있는 함수에서는 굳이

조회하는 함수에서와 했던것 처럼 for loop을 진행하면서 if 분기처리를 하지 않아도 된다.

- 즉 내부 알고리즘을 바꿀 수 있다.
```java
public void alertForMiscreant(List<Person> people) {
    // findMiscreant 메소드의 반환 값이 Blank인 경우 알람을 끄지 않아도 된다.
    if(!findMiscreant(people).isBlank()){
        setOffAlarms();
    }
}
```