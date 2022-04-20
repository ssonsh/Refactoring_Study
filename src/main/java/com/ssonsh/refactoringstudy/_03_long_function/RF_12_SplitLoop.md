# 리팩토링 12. 반복문 쪼개기

**냄새** : 긴 함수

→ **“반복문 쪼개기”** 를 통해 리팩토링 할 수 있다.

### 반복문 쪼개기 Split Loop

- 하나의 반복문에 여러 다른 작업을 하는 코드를 쉽게 찾아볼 수 있다.
- 해당 반복문을 수정할 떄 여러 작업을 모두 고려하며 코딩을 해야 한다.
- 반복문을 여러개로 쪼개면 보다 쉽게 이해하고 수정할 수 있다.

<aside>
🎈 성능 문제를 야기할 수 있지만 “리팩토링”은 “성능 최적화”와 별개의 작업이다.

- 리팩토링을 마친 이후 성능 최적화를 시도할 수 있다.
</aside>

**보통 한번에 반복문 안에서 여러 작업을 하는 경우가 많다.**

- 어차피 반복문 안에 있기 때문에 그런 것인다.
    - 효율/성능 적으로 맞다고 판단하기 떄문이다.
- 그러나 코드를 수정할 때 반복문 하나가 길면, 그 안의 모든 작업을 고려하여 작업해야 하는 단점이 있다.
- 정말 성능적으로 치명적인 부분이 아니라면 반복문을 각 작업마다 쪼개어 나눠놓고 추후 다시 성능을 고려해보는 것이 좋다.

**O(n) → 얼마나 성능 적으로 효율적인가?**

- 아무리 O(n)이 여러개가 있더라도 O(n) 이다.
- 물론 두배이긴 하지만.
- 알고리즘 적으로 O(n)이 O(n^2) 되는 것은 아니라는 것이다.

**실제로 접근을 해보고 성능을 측정하여 생각하자.**

---

**아래 코드를 기반으로 보자.**

- totalNumberOfEvents 를 기준으로 Loop 가 수행되고 있고
- 그 안에서는 issue의 Comments를 Loop 하고 있다.
    - 이 Comments Loop 에서는 아래와 같은 두가지 일을 하고 있다.
        - 과제를 제출한 참여자 찾기
        - 첫번째로 과제를 제출한 참여자 찾기

```java
for (int index = 1; index <= totalNumberOfEvents; index++) {
    int eventId = index;
    service.execute(new Runnable() {
        @Override
        public void run() {
            try {
                GHIssue issue = repository.getIssue(eventId);
                List<GHIssueComment> comments = issue.getComments();

                Date firstCreatedAt = null;
                Participant first = null;
                for (GHIssueComment comment : comments) {
                    Participant participant = findParticipant(comment.getUserName(), participants);
                    participant.setHomeworkDone(eventId);

                    if(firstCreatedAt == null || comment.getCreatedAt().before(firstCreatedAt)){
                        firstCreatedAt = comment.getCreatedAt();
                        first = participant;
                    }
                }
                latch.countDown();
            }
            catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    });
}
```

**리팩토링 해보자 - 1차**

- Loop 를 쪼개본다.
    - 과제를 제출한 참여자를 찾는 Loop
    - 가장 첫번째로 제출한 참여자를 찾는 Loop

```java
for (GHIssueComment comment : comments) {
    Participant participant = findParticipant(comment.getUserName(), participants);
    participant.setHomeworkDone(eventId);
}

Date firstCreatedAt = null;
Participant first = null;
for (GHIssueComment comment : comments) {
    Participant participant = findParticipant(comment.getUserName(), participants);
    if(firstCreatedAt == null || comment.getCreatedAt().before(firstCreatedAt)){
        firstCreatedAt = comment.getCreatedAt();
        first = participant;
    }
}
```

**리팩토링 해보자 - 2차**

- Split 된 Loop를 함수로 추출해본다.

```java
for (int index = 1; index <= totalNumberOfEvents; index++) {
    int eventId = index;
    service.execute(new Runnable() {
        @Override
        public void run() {
            try {
                GHIssue issue = repository.getIssue(eventId);
                List<GHIssueComment> comments = issue.getComments();

                checkHomwork(comments, participants, eventId);
                
                firstParticipantsForEachEvent[eventId - 1] = findFirst(comments, participants);

                latch.countDown();
            }
            catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    });
}

private Participant findFirst(List<GHIssueComment> comments, List<Participant> participants) throws IOException {
  Date firstCreatedAt = null;
  Participant first = null;
  for (GHIssueComment comment : comments) {
      Participant participant = findParticipant(comment.getUserName(), participants);
      if(firstCreatedAt == null || comment.getCreatedAt().before(firstCreatedAt)){
          firstCreatedAt = comment.getCreatedAt();
          first = participant;
      }
  }
  return first;
}

private void checkHomwork(List<GHIssueComment> comments, List<Participant> participants, int eventId) {
  for (GHIssueComment comment : comments) {
      Participant participant = findParticipant(comment.getUserName(), participants);
      participant.setHomeworkDone(eventId);
  }
}
```

---

```java
ExecutorService service = Executors.newFixedThreadPool(8);
CountDownLatch latch = new CountDownLatch(totalNumberOfEvents);

//

latch.await();
service.shutdown();
```

- MultiThread 로 처리하기 위한 ExecutorService와 CountDownLatch

`List<Participant> participants = new CopyOnWriteArrayList<>();`

- Concurrency Safe Collection 사용

---

이후 participant 와 같은 매개변수가 여러 메소드의 파라미터로 전달되고 있는데

이런부분들을 필드로 올려줄 수 있게 된다.

그렇게 되면 파라미터로 전달하던 부분들을 제거할 수 있게된다.

최종적으로 만들어진 StudyDashboard.java
```java
package com.ssonsh.refactoringstudy._03_long_function;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyDashboard {

    private int totalNumberOfEvents;
    private List<Participant> participants;
    private Participant[] firstParticipantsForEachEvent;

    public StudyDashboard(int totalNumberOfEvents) {
        this.totalNumberOfEvents = totalNumberOfEvents;
        this.participants = new CopyOnWriteArrayList<>();
        this.firstParticipantsForEachEvent = new Participant[this.totalNumberOfEvents];
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        StudyDashboard studyDashboard = new StudyDashboard(15);
        studyDashboard.print();
    }

    private void print() throws IOException, InterruptedException {
        GHRepository repository = findGithubRepository();

        checkGithubIssues(repository);

        new StudyPrinter(this.totalNumberOfEvents, this.participants).exeute();
    }

    private GHRepository findGithubRepository() throws IOException {
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        return repository;
    }

    private void checkGithubIssues(GHRepository repository) throws
        InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(8);
        CountDownLatch latch = new CountDownLatch(totalNumberOfEvents);

        for (int index = 1; index <= totalNumberOfEvents; index++) {
            int eventId = index;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        GHIssue issue = repository.getIssue(eventId);
                        List<GHIssueComment> comments = issue.getComments();
                        checkHomwork(comments, participants, eventId);
                        firstParticipantsForEachEvent[eventId - 1] = findFirst(comments, participants);
                        latch.countDown();
                    }
                    catch (IOException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
        }

        latch.await();
        service.shutdown();
    }

    private Participant findFirst(List<GHIssueComment> comments, List<Participant> participants) throws IOException {
        Date firstCreatedAt = null;
        Participant first = null;
        for (GHIssueComment comment : comments) {
            Participant participant = findParticipant(comment.getUserName(), participants);
            if(firstCreatedAt == null || comment.getCreatedAt().before(firstCreatedAt)){
                firstCreatedAt = comment.getCreatedAt();
                first = participant;
            }
        }
        return first;
    }

    private void checkHomwork(List<GHIssueComment> comments, List<Participant> participants, int eventId) {
        for (GHIssueComment comment : comments) {
            Participant participant = findParticipant(comment.getUserName(), participants);
            participant.setHomeworkDone(eventId);
        }
    }

    private Participant findParticipant(String username, List<Participant> participants) {
        return isNewParticipant(username, participants) ?
               createNewParticipant(username, participants) :
               findExistingParticipant(username, participants);
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

}
```