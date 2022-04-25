# 냄새 12. 반복되는 switch 문

### 반복되는 switch문 Repeated Switches

- 예전에는 swtich문이 한번만 등장해도 코드 냄새로 생각하고 다형성 적용을 권장했다.
    - [리팩토링 13. 조건문을 다형성으로 바꾸기](https://www.notion.so/13-7bcc42c268374bd79955e83235d49f25)
    - [리팩토링 32. 조건부 로직을 다형성으로 바꾸기](https://www.notion.so/32-b3bd1255a2e14b16b0f8e0d07afbb8e0)
- 하지만 최근에는 다형성이 꽤 널리 사용되고 있으며, 여러 프로그래밍 언어에서 보다 세련된 형태의 switch문을 제공하고 있다.
- 따라서 오늘날은 “**반복해서 등장하는 동일한 switch문**”을 냄새로 여기고 있다.
- 반복해서 동일한 switch문에 존재할 경우, 새로운 조건을 추가하거나 기존 조건을 변경할 때 모든 switch문을 찾아서 코드를 고쳐야 할지도 모른다.

---

아래 코드를 보며 살펴보자.

과거 **`Switch 문`**

```java
package com.ssonsh.refactoringstudy._12_repeated_swtiches;

public class SwitchImprovements {

    public int vacationHours(String type) {
        int result;
        switch (type) {
            case "full-time": result = 120;
            case "part-time": result = 80;
            case "temporal": result = 32;
            default: result = 0;
        }
        return result;
    }
}
```

위 switch 문은 심각한 버그가 있다.

switch 문에 안에서 case 걸리게 되면 “break”를 만나기 전 까지는 순차적으로 처리해나간다.

- 즉 위의 경우 3개 case에 걸렸다 하더라도 default 문에 걸려서
- result 값은 0으로 반환될 것이다.

현재 **`Switch Expression`** 이라 얘기하는 형태

- “→” 화살표를 사용한다.

```java
private EmployeeType employeeType(String typeValue) {
    return switch (typeValue) {
        case "engineer" -> new Engineer();
        case "manager" -> new Manager();
        case "salesman" -> new Salesman();
        default -> throw new IllegalArgumentException();
    };
}
```

    💡 자바 17버전을 사용할 경우 위와 같이 Switch Expression을 사용할 수 있다.


Switch Expression은 Swith문과 다르다.

- 결과를 담을 수 있다.
- break와 같은 키워드를 사용하지 않아도 된다.