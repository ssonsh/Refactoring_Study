# 리팩토링 9. 객체 통째로 넘기기

**냄새** : 긴 함수

→ **“객체 통째로 넘기기”** 를 통해 리팩토링 할 수 있다.

**객체 통째로 넘기기 Preserve Whole Object**

- 어떤 한 레코드에서 구할 수 있는 여러 값들을 함수에 전달하는 경우, 해당 매개변수를 레코드 하나로 교체할 수 있다.
- 매개변수 목록을 줄일 수 있다.
    - 향후 추가될 지 모르는 매개변수 까지도
- 이 기술을 적용하기 전 **의존성**을 고려해야 한다.
- 어쩌면 해당 메소드의 위치가 적절하지 않을 수 있다.
    - 기능 편애 “Feature Envy” 냄새에 해당하는 케이스인 경우

---

아래와 같은 케이스가 있다고 보자

```java
participants.forEach(p -> {
    String markdownForHomework = getMarkdownParticipant(p.username(), p.homework());
    writer.print(markdownForHomework);
});
```

```java
private String getMarkdownParticipant(String username, Map<Integer, Boolean> homework) {
  return String.format("| %s %s | %.2f%% |\n",
                       username,
                       checkMark(homework, this.totalNumberOfEvents),
                       getRate(this.totalNumberOfEvents, homework));
}
```

getMarkdownParticipant 메소드를 호출 할 때  두가지 매개변수를 전달하고 있다.

- p.username()
- p.homework()

→ 각각의 파라미터들이 추출되는 Participaint 객체 자체를 통째로 넘기는 것이다.

```java
String markdownForHomework = getMarkdownParticipant(p);
```

```java
private String getMarkdownParticipant(Participant p) {
    return String.format("| %s %s | %.2f%% |\n",
                         p.username(),
                         checkMark(p, this.totalNumberOfEvents),
                         getRate(this.totalNumberOfEvents, p));
}
```

**고려, 고민해볼 수 있는 사항**

이 메소드가 전달받는 객체를 전달받아서 사용하는 것이 맞는가?

아니면, 그 객체안에 있는 특정 필드를 받아서 사용하는 것이 맞는가?

- 다른 곳에서도 사용될 메소드라면 객체를 전달받는 것 보다 특정 필드
  (이 메소드에서 필요로 하는 녀석)를 전달받는 것이 더 이익을 얻을 수 있을 것이다.
- 이 메소드에서만 사용될 것 같다! 라고 하면 Preserve Whole Object를 적용해보는 것이다.

**과연 이 메서드는 이 위치가 적절한가?**

- 아래 getRate 함수는 참여자에 대한 참석률을 구하는 함수이다.

```java
private double getRate(Participant p) {
    long count = p.homework().values().stream()
                  .filter(v -> v == true)
                  .count();
    double rate = count * 100 / this.totalNumberOfEvents;
    return rate;
}
```

- 참여율을 구하기 위해 필요한 정보는 전달받는 매개변수 Participant 안에 들어있다.
    - p.homework()
    - 다만, this.totalNumberOfEvents 는 없다.
- 그렇다면?
    - this.totalNumberOfEvents 만 어떻게 해결하면 getRate 를 구할 수 있을 것 같다.

***즉, Move Method를 고려해볼 수 있다.***

이 getRate 라는 함수를 Participant 로 Move 해볼 수 있다.

**Participant.java**

```java
package com.ssonsh.refactoringstudy._03_long_function;

import java.util.HashMap;
import java.util.Map;

public record Participant(String username, Map<Integer, Boolean> homework) {
    public Participant(String username) {
        this(username, new HashMap<>());
    }

    public double getRate(double total) {
        long count = this.homework.values().stream()
                                  .filter(v -> v == true)
                                  .count();
        return count * 100 / total;
    }

    public void setHomeworkDone(int index) {
        this.homework.put(index, true);
    }

    public double getRate(int totalNumberOfEvents) {
        long count = homework.values().stream()
                             .filter(v -> v == true)
                             .count();
        double rate = count * 100 / totalNumberOfEvents;
        return rate;
    }
}
```

**사용**
```java
private String getMarkdownParticipant(Participant p) {
    return String.format("| %s %s | %.2f%% |\n",
                         p.username(),
                         checkMark(p),
                         p.getRate(this.totalNumberOfEvents));
}
```