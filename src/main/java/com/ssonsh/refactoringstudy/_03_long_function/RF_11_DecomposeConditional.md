# 리팩토링 11. 조건문 분해하기

**냄새** : 긴 함수

→ **“조건문 분해하기”** 를 통해 리팩토링 할 수 있다.

### 조건문 분해하기 Decompose Conditional

- 여러 조건에 따라 달라지는 코드를 작성하다 보면 종종 긴 함수가 만들어지는 것을 목격할 수 있다.
- “`**조건**`”과 “`**액션**`” 모두 “**`의도`**” 를 표현해야 한다.
- 기술적으로는 “**함수 추출하기**”와 동일한 리팩토링이지만 의도만 다를 뿐이다.

---

**아래 코드를 대상으로 확인해보자.**

```java
private Participant findParticipant(String username, List<Participant> participants) {
    boolean isNewUser = participants.stream().noneMatch(p -> p.username().equals(username));
    Participant participant = null;
    if (isNewUser) {
        participant = new Participant(username);
        participants.add(participant);
    } else {
        participant = participants.stream().filter(p -> p.username().equals(username)).findFirst().orElseThrow();
    }
    return participant;
}
```

**1차적으로 함수 추출하기 리팩토링 기술을 이용해서 개선시켜보자.**

```java
private Participant findParticipant(String username, List<Participant> participants) {
    Participant participant = null;
    if (isNewParticipant(username, participants)) {
        participant = createNewParticipant(username, participants);
    } else {
        participant = findExistingParticipant(username, participants);
    }
    return participant;
}

private Participant findExistingParticipant(String username, List<Participant> participants) {
    Participant participant;
    participant = participants.stream().filter(p -> p.username().equals(username)).findFirst().orElseThrow();
    return participant;
}

private Participant createNewParticipant(String username, List<Participant> participants) {
    Participant participant;
    participant = new Participant(username);
    participants.add(participant);
    return participant;
}

private boolean isNewParticipant(String username, List<Participant> participants) {
    return participants.stream().noneMatch(p -> p.username().equals(username));
}
```

**2차적으로 개선해보자.**

- 삼항 연산자를 통해 아래와 같이 한줄 코드로 개선시킬 수 있을 뿐더러
- 의미도 명확하게 표현해낼 수 있다.

```java
private Participant findParticipant(String username, List<Participant> participants) {
    return isNewParticipant(username, participants) ?
           createNewParticipant(username, participants) :
           findExistingParticipant(username, participants);
}
```

여기서도 냄새를 눈치 챘는가?

- 동일한 매개변수들 username, participants 가 계속 넘어가고 있다.
- 이 부분은 매개변수 객체 만들기를 이용하여 또한번 개선해낼 수 있을 것이다.