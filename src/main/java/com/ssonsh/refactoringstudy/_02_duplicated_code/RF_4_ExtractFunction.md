# 리팩토링 4. 함수 추출하기

**냄새** : 중복 코드

→ “**함수 추출하기**” 를 통해 리팩토링 할 수 있다.

**함수 추출하기 - Extract Function**

- **`“의도”`**와 **`“구현”`** 분리하기
    - 어떤 코드가 어떤 일을 하려고 하는지 잘 표현하고 있다면? “의도” 가 잘 표현되어 있다는 것이다.
- 무슨 일을 하는 코드인지 알아내려고 노력해야 하는 코드라면? 해당 코드를 함수로 분리하고
- 함수 이름으로 “무슨 일을 하는지” 표현할 수 있다.
- 한 줄 짜리 메소드도 괜찮은가??
    - 의도를 잘 드러낼 수 있다면 좋다 🙂
- 거대한 함수 안에 들어있는 주석은 추출한 함수를 찾는데 있어서 좋은 단서가 될 수 있다.

---

**Base Code 기준으로 한번 드려다 보자.**

```java
public static void main(String[] args) throws IOException {
    StudyDashboard studyDashboard = new StudyDashboard();
    studyDashboard.printReviewers();
    studyDashboard.printParticipants(15);
}
```

```java
private void printParticipants(int eventId) throws IOException {
  // Get github issue to check homework
  GitHub gitHub = GitHub.connect();
  GHRepository repository = gitHub.getRepository("whiteship/live-study");
  GHIssue issue = repository.getIssue(eventId);

  // Get participants
  Set<String> participants = new HashSet<>();
  issue.getComments().forEach(c -> participants.add(c.getUserName()));

  // Print participants
  participants.forEach(System.out::println);
}
```

<aside>
🎈 함수 내 작성된 주석은 함수를 추출하는데 좋은 힌트를 가져갈 수 있다.

</aside>

`printParticipants` 메소드를 보면 내부 구현으로

1. Github issue를 가져오는 행위와
2. issue에서 참여자들을 가져오는 행위
3. 그 참여자들을 출력하는 행위

위의 순서로 동작하고 있는 것을 확인할 수 있다.

어떤가? 짧기 때문에 그리 읽기 어렵진 않았을 것 같다.

그러나, 각 단계별 처리해야하는 코드들이 점점 길어진다면? 메소드 자체가 커지고 의도를 파악하기 보다는 “구현”을 들여다보면서 많은 시간을 허비하게 될 수 있다.

**함수 추출을 진행해보자.**

<aside>
🎈 구현부가 감춰지고 의도만 드러내고 있는 메소드 Body 로 변화되었다.

</aside>


```java
private void printParticipants (int eventId) throws IOException {
  // Get github issue to check homework
  GHIssue issue = getGhIssue(eventId);

  // Get participants
  Set<String> participants = getUsernames(issue);

  // Print participants
  print(participants);
}
```

```java
private void print(Set<String> participants) {
    participants.forEach(System.out::println);
}

private Set<String> getUsernames(GHIssue issue) throws IOException {
    Set<String> usernames = new HashSet<>();
    issue.getComments().forEach(c -> usernames.add(c.getUserName()));
    return usernames;
}

private GHIssue getGhIssue(int eventId) throws IOException {
    GitHub gitHub = GitHub.connect();
    GHRepository repository = gitHub.getRepository("whiteship/live-study");
    GHIssue issue = repository.getIssue(eventId);
    return issue;
}
```