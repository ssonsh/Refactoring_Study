# 리팩토링 36. 특이 케이스 추가하기

**냄새** : 임시 필드

→ **“특이 케이스 추가하기”** 를 통해 리팩토링 할 수 있다.

**특이 케이스 추가하기 Introduce Special Case**

- 어떤 필드의 특정한 값에 따라 동일하게 동작하는 코드가 반복적으로 나타난다면,
    - 해당 필드를 감싸는 “**특별한 케이스**”를 만들어 해당 조건을 표현할 수 있다.
- 이러한 매커니즘을 “**특이 케이스 패턴**”이라고 부르고
    - “**Null Object 패턴**”을 이러한 패턴의 특수한 형태라고 볼 수 있다.

    
    💡 **Null Object 패턴**
    Null인 경우를 별도의 클래스로 빼내는 것


---

아래 코드를 보며 살펴보자.

CustomerService.java

```java
package com.ssonsh.refactoringstudy._16_temporary_field._36_introduce_special_case;

public class CustomerService {

    public String customerName(Site site) {
        Customer customer = site.getCustomer();

        String customerName;
        if (customer.getName().equals("unknown")) {
            customerName = "occupant";
        } else {
            customerName = customer.getName();
        }

        return customerName;
    }

    public BillingPlan billingPlan(Site site) {
        Customer customer = site.getCustomer();
        return customer.getName().equals("unknown") ? new BasicBillingPlan() : customer.getBillingPlan();
    }

    public int weeksDelinquent(Site site) {
        Customer customer = site.getCustomer();
        return customer.getName().equals("unknown") ? 0 : customer.getPaymentHistory().getWeeksDelinquentInLastYear();
    }

}
```

customerName을 알아내는데 사용된 코드만 확인해보자.

```java
String customerName;
if (customer.getName().equals("unknown")) {
    customerName = "occupant";
} else {
    customerName = customer.getName();
}
```

- customer 객체의 getName()의 결과가 “unknown” 이면
    - occupant 라는 임의 필드 값으로 정의를 하고 있고
- “unknown”이 아니면 customer 객체의 getName() 결과를 할당하고 있다.

billingPlan 메서드 내부를 보자

```java
return customer.getName().equals("unknown") ? new BasicBillingPlan() : customer.getBillingPlan();
```

- customer 객체의 getName() 결과가 “unknown” 이면
    - new BasicBillingPlan() 을 처리하고
- “unknown” 이 아니면 customer 객체의 getBillingPlan() 을 따른다.

***→ weeksDeliquent 메서드도 동일한 매커니즘을 가지고 있다.***

**냄새 → unknown 이라는 케이스에 대한 부분이 반복되고 있다.**

이 unknown에 해당하는 특이한 케이스를 별도로 분리해낸다.

**UnknownCustomer.java**

```java
package com.ssonsh.refactoringstudy._16_temporary_field._36_introduce_special_case;

public class UnknownCustomer extends Customer{

    public UnknownCustomer() {
        super("unknown", null, null);
    }

    @Override
    public boolean isUnknown() {
        return true;
    }
}
```

위와 같이 Unknown 케이스에 대해 Customer를 재정의 하였다.

이제 Client에서 사용하는 Customer 객체를 어떤 것을 주입시킬지를 하도록 한다.

**Site.java**

- 인자로 넘어온 customer의 getName() 이 unknown 이면
- Customer 객체를 UnknownCustomer 로 할당하고
- 그렇지 않으면 매개변수로 넘어온 customer를 그대로 사용한다.

```java
package com.ssonsh.refactoringstudy._16_temporary_field._36_introduce_special_case;

public class Site {

    private Customer customer;

    public Site(Customer customer) {
        this.customer = customer.getName().equals("unknown") ? new UnknownCustomer() : customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
```

이로써, Site 객체를 통해 getCustomer()로 가져온 객체는

Customer 혹은 UnknownCustomer일 것이며 그에 따라 작업이 달라지게 될 것이다.

**CustomerService에서 Null Object 패턴도 사용될 수 있다.**

```java
public int weeksDelinquent(Site site) {
    Customer customer = site.getCustomer();
    return customer.getName().equals("unknown") ? 0 : customer.getPaymentHistory().getWeeksDelinquentInLastYear();
}
```

- 이 부분이며 unknown 이면 0 을 할당하는데 이것이
- PaymentHistory의 Null 인 것이다.

**NullPaymentHistory.java**

- PaymentHistory를 상속받은 NullPaymentHistory를 생성하고
- 생성자 매개변수로 0을 할당시켜줌으로써 위 로직과 동일한 역할을 하도록 변경해낼 수 있다.

```java
package com.ssonsh.refactoringstudy._16_temporary_field._36_introduce_special_case;

public class NullPaymentHistory extends PaymentHistory{
    public NullPaymentHistory() {
        super(0);
    }
}
```

이렇게 UnknownCustomer는 아래와 같이 특이한 케이스에 따라 동작하게 되는

- billingPlan 을 처리하고있고
- paymentHistory 를 처리하고 있다.

```java
package com.ssonsh.refactoringstudy._16_temporary_field._36_introduce_special_case;

public class UnknownCustomer extends Customer{

    public UnknownCustomer() {
        super("unknown", new BasicBillingPlan(),new NullPaymentHistory());
    }

    @Override
    public boolean isUnknown() {
        return true;
    }

    @Override
    public String getName() {
        return "occupant";
    }
}
```

위 CustomerService 에서 Unknown 케이스에 대해 리팩토링을 진행할 수 있다.

```java
package com.ssonsh.refactoringstudy._16_temporary_field._36_introduce_special_case;

public class CustomerService {

    public String customerName(Site site) {
        return site.getCustomer().getName();
    }

    public BillingPlan billingPlan(Site site) {
        return site.getCustomer().getBillingPlan();
    }

    public int weeksDelinquent(Site site) {
        return site.getCustomer().getPaymentHistory().getWeeksDelinquentInLastYear();
    }
}
```

→ 동일한 로직을 사용하되 다른 방법을 사용하게 된다.