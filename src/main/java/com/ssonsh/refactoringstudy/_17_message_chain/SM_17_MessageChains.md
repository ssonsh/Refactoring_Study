# 냄새 17. 메시지 체인

### 메시지 체인 Message Chains

- 레퍼런스를 따라 계속해서 메소드 호출이 이어지는 코드 (Chain)

```java
// 예시
this.member.getCredit().getLevel().getDescription()
```

- 해당 코드의 클라이언트가  코드 체인을 모두 이해해야 한다.
    - *개발하는 사람이 member가 Credit을 가지고 있고 Credit이 Level을 가지고 있고, Level이 Description을 알고 있어야 한다는 것이다.*
- 체인 중 일부가 변경된다면 ***클라이언트의 코드도 변경***해야 한다.

**관련 리팩토링**

- “**위임 숨기기(Hide Delegate)**”를 사용해 메시지 체인을 **`캡슐화`** 할 수 있다.
    - 클라이언트가 최소한의 정보만 알아도 되도록 캡슐화 시키는 것이다.
- “**함수 추출하기 (Exctract Function)**”로 메시지 체인 일부를 함수로 추출한 뒤
  ”**함수 옮기기(Move Function)**”으로 해당 함수를 적절히 이동할 수 있다.
    - [리팩토링 4. 함수 추출하기](https://www.notion.so/4-42dc95cdb4744117805ca807fa4db63e)
    - [리팩토링 25. 함수 옮기기](https://www.notion.so/25-fcf63963c6c44abd8fb6bb48bb90325e)