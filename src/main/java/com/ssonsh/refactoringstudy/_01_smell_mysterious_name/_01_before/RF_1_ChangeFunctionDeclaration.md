# 리팩토링 1. 함수 선언 변경하기

**냄새** : 이해하기 힘든 이름

→ “**함수 선언 변경하기**” 를 통해 리팩토링 할 수 있다.

**함수 선언 변경하기 - Change Function Declaration**

- 함수 이름 변경하기
- 메서드 이름 변경하기
- 매개변수 추가하기
- 매개변수 제거하기
- 시그니처 변경하기

---

- 좋은 이름을 가진 함수는 함수가 어떻게 구현되었는지 코드를 보지 않아도 **`이름만 보고도`** 이해할 수 있다.
- 좋은 이름을 찾아내는 방법?
    - *함수에 주석을 작성한 다음, 주석을 함수 이름으로 만들어 본다.*
- 함수의 **`매개변수`**는
    - 함수 내부의 문맥을 결정한다. (예. 전화번호 포매팅 함수)
    - 의존성을 결정한다. (예. Payment 만기일 계산 함수)

    <aside>
    🎈 모든 정보를 전달할 것인가? 정말 필요한 정보만 전달할 것인가?
    전달하는 정보에 따라서 그 함수가 할 수 있는 범위가 넓어진다. 

    - 주어진 환경에 따라서 이 함수에 필요한 문맥 정보를 정의하자.
    - 명확한 방법은 없다! 결국 환경에 맞추어 하고자 하는 역할을 명확히

    </aside>


---

### Base Code

```java
package com.ssonsh.refactoringstudy._01_smell_mysterious_name._01_before;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudyDashboard {

    private Set<String> usernames = new HashSet<>();

    private Set<String> reviews = new HashSet<>();

    private void studyReviews(GHIssue issue) throws IOException {
        List<GHIssueComment> comments = issue.getComments();
        for (GHIssueComment comment : comments) {
            usernames.add(comment.getUserName());
            reviews.add(comment.getBody());
        }
    }

    public Set<String> getUsernames() {
        return usernames;
    }

    public Set<String> getReviews() {
        return reviews;
    }

    public static void main(String[] args) throws IOException {
        GitHub gitHub = GitHub.connect();
        GHRepository repository = gitHub.getRepository("whiteship/live-study");
        GHIssue issue = repository.getIssue(30);

        StudyDashboard studyDashboard = new StudyDashboard();
        studyDashboard.studyReviews(issue);
        studyDashboard.getUsernames().forEach(System.out::println);
        studyDashboard.getReviews().forEach(System.out::println);
    }
}
```

studyDashboard.**studyReviews(issue);**

- 깃헙 레파지토리의 특정 이슈를 매개변수로 하여 리뷰정보를 조회하고자 한다.
- 함수 내에서는 매개변수로 전달받은 이슈의 Comments를 가지고 for loop 하여
- username과 review 정보를 처리한다.

### 고민

<aside>
🎈 과연 이 함수의 이름이 적절한가? 매개변수가 적절한가?

</aside>

**studyReviews 메소드명 이 과연 최선인가!?**

- study를 Review 한다는 것인가? Review를 읽어온다는 것인가?
    - 가이드와 같이 주석을 먼저 달아보자

```java
/**
 * 스터디 리뷰 이슈에 작성되어 있는 리뷰어 목록과 리뷰를 읽어온다.
 * @param issue
 * @throws IOException
 */
```

**로딩**이라는 것이 들어가면 더 명확하지 않을까?

AS-IS

- studyReviews(GHIssue issue)

**TO-BE**

- `loadReviews(GHIssue issue)`

**메소드의 매개변수가 과연 최선인가?**

- 상황에 따라 다를 수 있음으로 그 상황을 명확히 파악하는 것이 더 중요하다.
- 위 코드의 경우 GHIssue 를 매개변수로 전달받고 있다.
- 하지만, 처리하고자 하는 GHIssue는 동적이지 않다.
    - 즉, 특정 Issue에 대해서 처리를 하고 있다.
    - `GHIssue issue = repository.getIssue(30);`

- 그렇다면 굳이 메소드 밖에서 조회한 Issue를 매개변수로 전달할 필요가 없어진다.
    - 이 상황에서는 그렇다는 것이다.

```java
/**
 * 스터디 리뷰 이슈에 작성되어 있는 리뷰어 목록과 리뷰를 읽어온다.
 * @throws IOException
 */
private void loadReviews() throws IOException {
    GitHub gitHub = GitHub.connect();
    GHRepository repository = gitHub.getRepository("whiteship/live-study");
    GHIssue issue = repository.getIssue(30);

    List<GHIssueComment> comments = issue.getComments();
    for (GHIssueComment comment : comments) {
        usernames.add(comment.getUserName());
        reviews.add(comment.getBody());
    }
}
```