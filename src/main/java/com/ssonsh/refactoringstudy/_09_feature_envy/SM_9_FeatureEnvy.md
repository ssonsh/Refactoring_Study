# 냄새 9. 기능 편애

### 기능 편애 Feature Envy

<aside>
💡 기능에 욕심을 내다보면, 다른 모듈에 있어야 하는 기능 조차 본인이 가지게 되는 현상을 얘기하는 것이라 보면 된다.

</aside>

- 어떤 모듈에 있는 함수가 다른 모듈에 있는 데이터나 함수를 더 많이 참조하는 경우 발생한다.
    - 예) 다른 객체의 Getter를 여러개 사용하는 메소드

        <aside>
        💡 모듈 = 클래스로 볼 수 있다.

        </aside>


**관련 리팩토링**

- “**함수 옮기기(Move Function)**” 를 사용해서 함수를 적절한 위치로 옮긴다.
    - [리팩토링 25. 함수 옮기기](https://www.notion.so/25-fcf63963c6c44abd8fb6bb48bb90325e)
    - 함수 **일부분만** 다른 곳의 데이터와 함수를 많이 참조한다면
    - “**함수 추출하기(Extract Function)**”로 함수를 나눈 다음에 함수를 옮길 수 있다.
- 만약 여러 모듈을 참조하고 있다면? 그 중에서 가장 많은 데이터를 참조하는 곳으로 옮기거나, 함수를 여러개로 쪼개서 각 모듈로 분산 시킬 수도 있다.
- 데이터와 해당 데이터를 참조하는 행동을 같은 곳에 두도록 하자.
- 예외적으로, 데이터와 행동을 분리한 디자인 패턴 (전략 패턴 혹은 방문자 패턴)을 적용할 수도 있다.



모듈화를 한다는 것은 기본적으로

- 관련있는 데이터와 행동, Behavior, Function, Method를 한 곳에 위치시키는 것이다.
- 하지만 행동을 조건에 의해 달리 가져가야 하는 디자인 패턴에서는 예외적으로 적용할 수 있다.

---

Bill.java

```java
package com.ssonsh.refactoringstudy._09_feature_envy;

public class Bill {

    private ElectricityUsage electricityUsage;

    private GasUsage gasUsage;

    public double calculateBill() {
        var electicityBill = electricityUsage.getAmount() * electricityUsage.getPricePerUnit();
        var gasBill = gasUsage.getAmount() * gasUsage.getPricePerUnit();
        return electicityBill + gasBill;
    }

}
```

위 클래스를 보자

Bill 클래스는 Feature, 기능에 욕심을 내고 있다.

calculateBill() 메서드를 통해 유추해볼 수 있는데 그 이유는

- electrictyUsage, gasUsage 의 Feature, 기능들을 적극적으로 사용하고 있다.

electricityBill, gasBill 은 각 클래스에서 계산할 수 있으나

굳이 Bill이라는 클래스에서 계산하다보니 다른 클래스의 참조가 증가하게 된 상황이라 볼 수 있다.

- 각 계산은 각 클래스로 이동시키고
- 객체의 기능을 활용하도록 하자.

```java
package com.ssonsh.refactoringstudy._09_feature_envy;

public class Bill {

    private ElectricityUsage electricityUsage;

    private GasUsage gasUsage;

    public double calculateBill() {
        return electricityUsage.getElecticityBill() + gasUsage.getGasBill();
    }

}
```