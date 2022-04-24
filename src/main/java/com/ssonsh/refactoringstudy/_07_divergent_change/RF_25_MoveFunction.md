# 리팩토링 25. 함수 옮기기

**냄새** : 뒤엉킨 변경

→ **“함수 옮기기”** 를 통해 리팩토링 할 수 있다.

**함수 옮기기 Move Function**

- 모듈화가 잘 된 소프트웨어는 최소한의 지식만으로 프로그램을 변경할 수 있다.
- 관련있는 함수나 필드가 모여있어야 더 쉽게 찾고 이해할 수 있다.
- 하지만 관련있는 함수나 필드가 항상 고정적인 것은 아니기 때문에 때에 따라 옮겨야 할 필요가 있다.
- **함수를 옮겨야 하는 경우**
    - 해당 함수가 **다른 문맥(클래스)에 있는 데이터(필드)를 더 많이 참조**하는 경우
    - 해당 함수를 **다른 클라이언트 (클래스)에서도 필요**로 하는 경우
- 함수를 옮겨갈 새로운 문맥(클래스)이 필요한 경우
    - **“여러 함수를 클래스로 묶기(Combine Functions info Class)”**
    - **“클래스 추출하기(Extract Class)” 를 사용한다.**
- 함수를 옮길 적당안 위치를 찾기가 어렵다면? 그대로 두어도 괜찮다. 언제든 나중에 옮길 수 있다.

모듈화가 잘 된 소프트웨어

- 어떤 변경사항을 만들어야 할 때 변경해야 하는 주제가 있을 것이다.
- db 변경, 결제 수단 추가, 등등..
- 이런 업무에 따라 파악해야 하는 지식이 최소한으로 한정된다면 이는 모듈화가 잘된 것이라 할 수 있다.

반대로, 그렇지 않고 여러 곳을 살펴봐야만 알 수 있다면 모듈화가 잘 되어 있지 않다고 볼 수 있다.

→ 응집도/결합도가 제대로 관리되고 있지 않다는 것이다.

<aside>
🎈 응집도

관련있는 함수나 필드들이 한 곳에 잘 모여 있는가?

높을 수록 좋은 것이다.

낮다는 것은 관련있는 함수나 필드들이 여러 곳에 흩어져 있다는 것이다.

즉, 프로그램을 파악하기 위해 흩어진 곳으로 컨택스트 스위칭이 되어야 하는 것이다.

응집도가 높으면 한 곳에 모여있기 때문에 문맥 파악에 큰 이점을 가져갈 수 있고 변경에도 용이하다.

</aside>

**처음부터 응집도가 높은, 즉 모듈화가 잘 된 소프트웨어를 만들 수 있다고 믿는가?**

- 그럴 수 있겠지만, 처음부터 쉽지 않을 것이다.
- 즉, 만들어지고 난 이후 이런 응집도가 관리될 수 있도록 코드리뷰/리팩토링이 진행되어야 하는 것이다.

---

 

아래코드를 보고 파악해보자.

**AccountType.java**

```java
package com.ssonsh.refactoringstudy._07_divergent_change._25_move_function;

public class AccountType {
    private boolean premium;

    public AccountType(boolean premium) {
        this.premium = premium;
    }

    public boolean isPremium() {
        return this.premium;
    }
}
```

**Account.java**

```java
package com.ssonsh.refactoringstudy._07_divergent_change._25_move_function;

public class Account {

    private int daysOverdrawn;

    private AccountType type;

    public Account(int daysOverdrawn, AccountType type) {
        this.daysOverdrawn = daysOverdrawn;
        this.type = type;
    }

    public double getBankCharge() {
        double result = 4.5;
        if (this.daysOverdrawn() > 0) {
            result += this.overdraftCharge();
        }
        return result;
    }

    private int daysOverdrawn() {
        return this.daysOverdrawn;
    }

    private double overdraftCharge() {
        if (this.type.isPremium()) {
            final int baseCharge = 10;
            if (this.daysOverdrawn <= 7) {
                return baseCharge;
            } else {
                return baseCharge + (this.daysOverdrawn - 7) * 0.85;
            }
        } else {
            return this.daysOverdrawn * 1.75;
        }
    }
}
```

여기서 overdraftCharge() 메서드의 위치가 적절할까? 

**this.type.isPreminum()** 과 같이 AccountType의 값을 사용하고 있다.

- 이런 부분들이 많아진다면? 그 곳으로 위치시키는 것이 적절할 것이다.
- *현재 여기선 하나만 바라보고 있기 떄문에 사실 옮겨도 되고 그대로 둬도 될 것 같다.*

리팩토링 학습상 옮겨보도록 하자.

```java
package com.ssonsh.refactoringstudy._07_divergent_change._25_move_function;

public class AccountType {
    private boolean premium;

    public AccountType(boolean premium) {
        this.premium = premium;
    }

    public boolean isPremium() {
        return this.premium;
    }

    double overdraftCharge(int daysOverdrawn) {
        if (isPremium()) {
            final int baseCharge = 10;
            if (daysOverdrawn <= 7) {
                return baseCharge;
            } else {
                return baseCharge + (daysOverdrawn - 7) * 0.85;
            }
        } else {
            return daysOverdrawn * 1.75;
        }
    }
}
```

- 이렇게 옮기면서도 고민 될 수 있는 것이 있다.
- overdraftCharge 메소드의 매개변수로 Account 를 받을 것인가? 아니면 정말 필요로 하는 매개변수들을 받을 것인가?
    - 만약 Account를 받게 된다면 AccountType과 Account 간에 강력한 의존관계가 생성되는 것이다.
- 그렇다면 필요로 하는 매개변수만 받는 것이 타당할까?
    - 현재는 그렇다고 본다. 왜? 하나니깐
    - 만약 많아진다면?
    - 매개변수 클래스화 시키기를 활용하여 파라미터 수를 줄여낼 수도 있을 것 같다.
- 즉, 포인트는 굳이 의존관계를 강력하게 만들어낼 필요가 없을 것 같다.

혹은, Account의 여러 필드를 참조하게 되는 overdraftChange 메소드로 변화한다면? 

- AccountType으로 이동시킨 이 메소드를
- Account로 다시 가져오는게 타당할 수 있다.
