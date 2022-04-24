# 리팩토링 27. 필드 옮기기

**냄새** : 산탄총 수술

→ **“필드 옮기기”** 를 통해 리팩토링 할 수 있다.

**필드 옮기기 Move FIeld**

- **좋은 데이터 구조**를 가지고 있다면
    - 해당 데이터에 기반한 어떤 행위를 코드로(메소드나 함수) 옮기는 것도 간편하고 단순해진다.
- **처음에는 타당해 보였던 설계적인 의사 결정**도 프로그램이 다루고 있는 도메인과 데이터 구조에 대해 더 많이 익혀나가면서, **틀린 의사 결정으로 바뀌는 경우도 있다.**
- 필드를 옮기는 단서
    - 어떤 데이터를 항상 어떤 레코드와 함께 전달하는 경우
        - 특정 메소드에 클래스와, 그 외 변수가 함께 파라미터로 전달된다면?
        - 그리고 이 조합이 여러곳에서 사용된다면?
        - 그 외 변수가 클래스 안으로 들어가야 하는 단서가 된다.
    - 어떤 레코드를 변경할 때 다른 레코드에 있는 필드를 변경해야 하는 경우
        - 바꾸려는 레코드/클래스가 아닌 다른 레코드/클래스도 함께 바꿔줘야 한다면?
        - 바꿔야 하는 대상을 이동시켜야 하는 단서가 될 수 있다.
    - 여러 레코드에 동일한 필드를 수정해야 하는 경우

    <aside>
    🎈 여기서의 레코드는 클래스 또는 객체로 대체할 수 있음.

    </aside>
    
  ---

  아래 코드를 보며 확인해보자.

  Customer.java

    ```java
    package com.ssonsh.refactoringstudy._08_shotgun_surgery._27_move_field;
    
    import java.math.BigDecimal;
    import java.time.LocalDate;
    
    public class Customer {
    
        private String name;
    
        private double discountRate;
    
        private CustomerContract contract;
    
        public Customer(String name, double discountRate) {
            this.name = name;
            this.discountRate = discountRate;
            this.contract = new CustomerContract(dateToday());
        }
    
        public double getDiscountRate() {
            return discountRate;
        }
    
        public void becomePreferred() {
            this.discountRate += 0.03;
            // 다른 작업들
        }
    
        public double applyDiscount(double amount) {
            BigDecimal value = BigDecimal.valueOf(amount);
            return value.subtract(value.multiply(BigDecimal.valueOf(this.discountRate))).doubleValue();
        }
    
        private LocalDate dateToday() {
            return LocalDate.now();
        }
    }
    ```

  CustomerContract.java

    ```java
    package com.ssonsh.refactoringstudy._08_shotgun_surgery._27_move_field;
    
    import java.time.LocalDate;
    
    public class CustomerContract {
    
        private LocalDate startDate;
    
        public CustomerContract(LocalDate startDate) {
            this.startDate = startDate;
        }
    }
    ```

  Test.java

    ```java
    package com.ssonsh.refactoringstudy._27_move_field;
    
    import com.ssonsh.refactoringstudy._08_shotgun_surgery._27_move_field.Customer;
    import org.junit.jupiter.api.Test;
    
    import static org.junit.jupiter.api.Assertions.assertEquals;
    
    class CustomerTest {
    
        @Test
        void applyDiscount() {
            Customer customer = new Customer("keesun", 0.5);
            assertEquals(50, customer.applyDiscount(100));
    
            customer.becomePreferred();
            assertEquals(47, customer.applyDiscount(100));
        }
    
    }
    ```
    
  ---

  옮겨야 할 필드가 있다면, 필드에 접근하는 코드를 Getter, Setter로 감싸서 접근할 수 있도록 캡슐화 해줄 수 있다.

    - discountRate 필드에 대해 Getter, Setter 처리

    ```java
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
    
    public double getDiscountRate() {
        return discountRate;
    }
    ```

    - 그리고 캡슐화한 필드인 discountRate을 변경하거나 참조하는 코드를 메서드로 활용하도록 변경해준다.

    ```java
    public void becomePreferred() {
        this.setDiscountRate(this.getDiscountRate() + 0.03);
        // 다른 작업들
    }
    
    public double applyDiscount(double amount) {
        BigDecimal value = BigDecimal.valueOf(amount);
        return value.subtract(value.multiply(BigDecimal.valueOf(this.getDiscountRate()))).doubleValue();
    }
    ```

  그 다음 필드를 옮겨간다.

    - discountRate 필드를 CustomerContract로 옮겨보자.

    ```java
    package com.ssonsh.refactoringstudy._08_shotgun_surgery._27_move_field;
    
    import java.time.LocalDate;
    
    public class CustomerContract {
    
        private LocalDate startDate;
    
        private double discountRate;
    
        public CustomerContract(double discountRate, LocalDate startDate) {
            this.startDate = startDate;
            this.discountRate = discountRate;
        }
    
        public double getDiscountRate() {
            return discountRate;
        }
    
        public void setDiscountRate(double discountRate) {
            this.discountRate = discountRate;
        }
    }
    ```

- discountRate 필드가 CustomerContract 로 이관되었다.
- 그렇다면 더 이상 Customer 클래스에서는 discountRate가 필요없어 짐으로 제거할 수 있다.
    - 제거하고 나면 어떻게 되는가?
    - discountRate 필드를 참조하거나 변경하던 로직에서 컴파일 오류가 발생될 것이다.
    - 그러나 우리는 사전에 discountRate 필드를 직접 참조/수정 하는 것을 메소드로 추상화시켰다.
    - 그럼으로 discountRate 를 참조/수정하는 메소드만 변경해주면 된다.

```java
package com.ssonsh.refactoringstudy._08_shotgun_surgery._27_move_field;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Customer {

    private String name;

    private CustomerContract contract;

    public Customer(String name, double discountRate) {
        this.name = name;
        this.contract = new CustomerContract(discountRate, dateToday());
    }

    public void setDiscountRate(double discountRate) {
        this.contract.setDiscountRate(discountRate);
    }

    public double getDiscountRate() {
        return this.contract.getDiscountRate();
    }

    public void becomePreferred() {
        this.setDiscountRate(this.getDiscountRate() + 0.03);
        // 다른 작업들
    }

    public double applyDiscount(double amount) {
        BigDecimal value = BigDecimal.valueOf(amount);
        return value.subtract(value.multiply(BigDecimal.valueOf(this.getDiscountRate()))).doubleValue();
    }

    private LocalDate dateToday() {
        return LocalDate.now();
    }
}
```