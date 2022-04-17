# 리팩토링 3. 필드 이름 바꾸기

**냄새** : 이해하기 힘든 이름

→ “필드 **이름 바꾸기**” 를 통해 리팩토링 할 수 있다.

**필드 이름 바꾸기 Rename Field**

**Record 자료구조**의 필드 이름은 **`프로그램 전반에 걸쳐 참조될 수 있기 때문`**에 `**매우 중요**`하다.

- **Record 자료구조**
    - 특정 데이터와 관련있는 필드를 묶어놓은 자료구조
    - 파이썬의 Dictionary (dicts)
    - C#의 Record
    - 자바 14 버전부터 지원하는 record 키워드
    - 자바에서는 Getter, Setter 메소드 이름도 필드의 이름과 비슷하게 간주할 수 있다.


---

**Base Code 에 정의된 필드**

```java
private Set<String> usernames = new HashSet<>();
private Set<String> reviews = new HashSet<>();
```

usernames

- 최초에는 “리뷰를 작성한 사용자 이름들”이라는 의미로 usernames가 적절하다고 판단되었다.
- 하지만 다시 살펴보니 usernames 사용자 이름들 이라는 이름보다는
- 리뷰(review)를 작성한 사용자임으로 리뷰어들(reviewers) 라는 의미가 더 적절해 보인다.

```java
private Set<String> reviewers = new HashSet<>();
```

---

**Java 14 버전 이후부터 지원하는 `record 키워드`**

- record를 이용하여 reviewers 와 reivews 컬랙션을 변경하여 사용해보자.

**StudyReview.java**

- class 가 아니라 record 이다. 🙂

<aside>
🎈 Value Object나 DTO로 사용하던 것을 record로 대체할 수 있다!

</aside>

```java
package com.ssonsh.refactoringstudy._01_smell_mysterious_name._01_before;

public record StudyReview(String reviewers, String reviews) {
}
```
**사용하는 코드를 변경해보자.**

```java
// 생략..
private Set<StudyReview> studyReviews = new HashSet<>();
// 생략..

List<GHIssueComment> reviews = issue.getComments();
for (GHIssueComment review : reviews) {

    // AS-IS
    /*
    reviewers.add(review.getUserName());
    this.reviews.add(review.getBody());
     */

    // TO-BE. use Record
    studyReviews.add(new StudyReview(review.getUserName(), review.getBody()));
}
```

```java
public static void main(String[] args) throws IOException {
  StudyDashboard studyDashboard = new StudyDashboard();
  studyDashboard.loadReviews();
  // AS-IS
  /*
  studyDashboard.getReviewers().forEach(name -> System.out.println(name));
  studyDashboard.getReviews().forEach(review -> System.out.println(review));
   */
  
  // TO-BE
  studyDashboard.getStudyReviews().forEach(System.out::println);
}
```