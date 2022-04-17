# 리팩토링 2. 변수 이름 바꾸기

**냄새** : 이해하기 힘든 이름

→ “**변수 이름 바꾸기**” 를 통해 리팩토링 할 수 있다.

**변수 이름 바꾸기 Rename Variable**

- 더 많이 사용되는 변수일수록 그 이름이 더욱 중요하다.
    - 람다식에서 사용하는 변수 vs 함수의 매개변수
    - 즉 범위 Scope에 따른 변수 정의에 대해 고민해볼 수 있다.
- 다이나믹 타입을 지원하는 언어에서는 타입을 이름에 넣기도 한다.
- 여러 함수에 걸쳐 쓰는 필드 이름에는 더 많은 고민이 필요하다.

<aside>
🎈 처음부터 완벽한 이름을 정의하기 어렵다는 것을 인정하고 들어가자 🙂

</aside>

---

**람다식에서 사용하는 변수와 매소드 내에서 사용하는 변수에 대해 살펴보자.**

**람다식에서 사용하는 변수**

```java
studyDashboard.getUsernames().forEach(name -> System.out.println(name));
studyDashboard.getReviews().forEach(review -> System.out.println(review));
```

- getUsernames() 를 순회하면서 “name” 이라는 값으로 변수를 할당했다.
- 람다식에서 사용하는 변수는 가장 좁은 Scope이라 할 수 있다.
- 대부분 람다식에서 사용되는 변수는 **`어떤 것인지 이미 알고 있다.`**
    - 어떤 타입에? 어떤 데이터가?
- 위와 같이 name, review 와 같이 지정할 수 있고
- 명확하기 때문에 n, r 과 같은 약어로 정의하여 처리할 수도 있다.

**메소드 내에서 사용하는 변수**

```java
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

`List<GHIssueComment> comments = issue.getComments();`

- 실제 의미 자체는 issue 객체 안에 있는 getComments() 로 comments 를 정의한 것이 맞다.
- 하지만, 지금 우리는 코멘트(comment) 가 아니라 리뷰(review)라는 의미로 사용하고자 하고 있다.
- 메소드 명을 보자 → 리뷰를 읽어오는 메소드 “loadReviews()” 인데
    - 메소드 내부에는 리뷰(review)가 아무것도 없다.
- 아래와 같이 comments/comment → reviews/review 로 변경해보았다.

```java
List<GHIssueComment> reviews = issue.getComments();
for (GHIssueComment review : reviews) {
    usernames.add(review.getUserName());
    this.reviews.add(review.getBody());
}
```