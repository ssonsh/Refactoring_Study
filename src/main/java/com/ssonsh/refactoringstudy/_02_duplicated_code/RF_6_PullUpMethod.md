# 리팩토링 6. 메소드 올리기

**냄새** : 중복 코드

→ “**메소드 올리기**” 를 통해 리팩토링 할 수 있다.

**메소드 올리기 - Pull Up Method**

- 중복 코드는 당장 잘 작동하더라도 미래에 버그를 만들어 낼 빌미를 제공한다.
    - A에서 코드 고치고, B에는 반영하지 않은 경우
- 여러 하위 클래스에 동일한 코드가 있다면, 손쉽게 이 방법을 적용할 수 있다.
- **비슷하지만 일부 값만 다른 경우라면,**
  “**`함수 매개변수화하기(Parameterize Function)`**” 리팩토링을 적용한 이후에, 이 방법을 사용할 수 있다.

    <aside>
    🎈 이 경우가 많이 있을 것 같다.

    </aside>

- 하위 클래스에 있는 코드가 상위 클래스가 아닌 **하위 클래스 기능에 의존하고 있다면,**
  ”**`필드 올리기(Pull Up Field)`**”를 적용한 이후에 이 방법을 적용할 수 있다.
- **두 메소드가 비슷한 절차를 따르고 있다면**, “**`템플릿 메소드 패턴`**” 을 적용할 수 있다.

---

### Base Code

**Dashboard.java**

```java
package com.ssonsh.refactoringstudy._02_duplicated_code.pull_up_method;

import java.io.IOException;

public class Dashboard {

    public static void main(String[] args) throws IOException {
        ReviewerDashboard reviewerDashboard = new ReviewerDashboard();
        reviewerDashboard.printReviewers();

        ParticipantDashboard participantDashboard = new ParticipantDashboard();
        participantDashboard.printParticipants(15);
    }
}
```

**[ParticipantDashboard.java](http://ParticipantDashboard.java) (extends Dashboard)**

```java
package com.ssonsh.refactoringstudy._02_duplicated_code.pull_up_method;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ParticipantDashboard extends Dashboard {

    public void printParticipants(int eventId) throws IOException {
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

}
```

[**ReviewerDashboard.java](http://ReviewerDashboard.java) (extends Dashboard)**

```java
package com.ssonsh.refactoringstudy._02_duplicated_code.pull_up_method;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ReviewerDashboard extends Dashboard {

    public void printReviewers() throws IOException {
        // Get github issue to check homework
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(30);

        // Get reviewers
        Set<String> reviewers = new HashSet<>();
        issue.getComments().forEach(c -> reviewers.add(c.getUserName()));

        // Print reviewers
        reviewers.forEach(System.out::println);
    }

}
```

ParticipantDashboard 와 ReviewerDashboard 의 코드를 보면

상당히 비슷한 부분이 있는 것을 확인할 수 있다.

- 이슈 만들고
- username 가져와서
- 출력해주는

ParticipantDashboard의 `printParticipants(int eventId)`

ReviewerDashboard의 `printReviewers()`

위 2개의 메소드 내용은 하는 행위는 같지만 조금 다르다.

ParticipantDashboard의 print는 “int eventId” 로 파라미터를 전달받고 있고,

전달받은 파라미터로 이슈를 조회하고 있다.

그러나 ReviewerDashboard의 print 는 파라미터를 전달받지 않고 내부적으로 고정적인 값으로 이슈를 조회하고 있다.

**파라미터라이즈 리팩토링을 이용하여 ReviwerDashboard를 개선할 수 있다.**

**AS-IS**

```java

public void printReviewers() throws IOException {
  // Get github issue to check homework
  GitHub gitHub = GitHub.connect();
  GHRepository repository = gitHub.getRepository("whiteship/live-study");
  GHIssue issue = repository.getIssue(30);

  // Get reviewers
  Set<String> reviewers = new HashSet<>();
  issue.getComments().forEach(c -> reviewers.add(c.getUserName()));

  // Print reviewers
  reviewers.forEach(System.out::println);
}

```

**`TO-BE`**

```java
public void printReviewers() throws IOException{
    printUsernames(30);
}

private void printUsernames(int eventId) throws IOException {
    // Get github issue to check homework
    GitHub gitHub = GitHub.connect();
    GHRepository repository = gitHub.getRepository("whiteship/live-study");
    GHIssue issue = repository.getIssue(eventId);

    // Get reviewers
    Set<String> reviewers = new HashSet<>();
    issue.getComments().forEach(c -> reviewers.add(c.getUserName()));

    // Print reviewers
    reviewers.forEach(System.out::println);
}
```

**[ParticipantDashboard.java](http://ParticipantDashboard.java) 에 있었던 메소드 명을 변경해보자.**

- printParticipants → printUsernames

**AS-IS**

```java
public void printParticipants(int eventId) throws IOException {
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

**`TO-BE`**

```java
public void printUsernames(int eventId) throws IOException {
	..
}
```

이렇게 되면 어떻게 되는가?

ParticipantDashboard 와 ReviwerDashboard에

동일한 역할을 수행하는 `printUsernames(int eventId)` 가 구성되게 된다.

**→ 이 때 Pull Up Method를 이용한다.**

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/82a3b48b-5904-4537-8a49-2eda5a37bd18/Untitled.png)

**위와 같이 진행되게 되면?**

- Pull Up 되어 상위 클래스인 Dashboard 클래스에 메소드가 올라간다.

```java
public class Dashboard {

    public static void main(String[] args) throws IOException {
        ReviewerDashboard reviewerDashboard = new ReviewerDashboard();
        reviewerDashboard.printReviewers();

        ParticipantDashboard participantDashboard = new ParticipantDashboard();
        participantDashboard.printUsernames(15);
    }

    public void printUsernames(int eventId) throws IOException {
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
}
```

ParticipantDashboard.java

```java
package com.ssonsh.refactoringstudy._02_duplicated_code.pull_up_method;

public class ParticipantDashboard extends Dashboard {

}
```

ReviewerDashboard.java