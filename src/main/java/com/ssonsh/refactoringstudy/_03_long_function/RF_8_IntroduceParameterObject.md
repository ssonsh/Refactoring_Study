# 리팩토링 8. 매개변수 객체 만들기

**냄새** : 긴 함수

→ **“매개변수 객체 만들기”** 를 통해 리팩토링 할 수 있다.

**매개변수 객체 만들기 Introduce Parameter Object**

- 같은 매개변수들이 여러 메소드에 걸쳐 나타난다면 그 매개변수들을 묶은 자료구조를 만들 수 있다.
- 그렇게 만들어낸 자료구조는?
    - 해당 데이터간의 관계를 보다 명시적으로 나타낼 수 있다.
    - 함수에 전달할 매개변수 개수를 줄일 수 있다.
    - 도메인을 이해하는데 중요한 역할을 하는 클래스로 발전할 수 있다.


---

아래 코드를 보자

```java
private double getRate(int totalNumberOfEvents, Participant p) {
    long count = p.homework().values().stream()
                  .filter(v -> v == true)
                  .count();
    double rate = count * 100 / totalNumberOfEvents;
    return rate;
}

private String getMarkdownParticipant(int totalNumberOfEvents, Participant p) {
    return String.format("| %s %s | %.2f%% |\n",
                         p.username(),
                         checkMark(p, totalNumberOfEvents),
                         getRate(totalNumberOfEvents, p));
}
```

- 두 메소드에 동일하게 “totalNumberOfEvents” 와 “Participant” 매개변수가 같이 넘어가고 있는 것을 볼 수 있다.

<aside>
🎈 Class를 활용하는게 보편적으로 사용할 수 있을 것 같고
여기서는  14버전부터 제공되는 reccord 를 이용하여 매개변수 객체 만들기를 사용해본다.

</aside>




```java
package com.ssonsh.refactoringstudy._03_long_function;

public record ParticipantPrinter(int totalNumberOfEvents, Participant p) {}
```

```java
private double getRate(ParticipantPrinter participantPrinter) {
    long count = participantPrinter.p().homework().values().stream()
                                   .filter(v -> v == true)
                                   .count();
    double rate = count * 100 / participantPrinter.totalNumberOfEvents();
    return rate;
}

private String getMarkdownParticipant(ParticipantPrinter participantPrinter) {
    return String.format("| %s %s | %.2f%% |\n",
                         participantPrinter.p().username(),
                         checkMark(participantPrinter.p(), participantPrinter.totalNumberOfEvents()),
                         getRate(participantPrinter));
}
```

다르게, int totalNumberOfEvents 를 필드 값으로 지정해볼 수 있을 것 같다.
→ 이후 이 정보를 파라미터로 사용하던 메서드들을 조정할 수 있다.

```java
private int totalNumberOfEvents;

public static void main(String[] args) throws IOException, InterruptedException {
    StudyDashboard studyDashboard = new StudyDashboard(15);
    studyDashboard.print();
}

public StudyDashboard(int totalNumberOfEvents) {
    this.totalNumberOfEvents = totalNumberOfEvents;
}
```