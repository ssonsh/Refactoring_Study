# 리팩토링 7. 임시 변수를 질의 함수로 바꾸기

**냄새** : 긴 함수

→ **“임시 변수를 질의 함수로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**임시 변수를 질의 함수로 바꾸기 Replace Temp with Query**

- 변수를 사용하면 반복해서 동일한 식을 계산하는 것을 피할 수 있고
    - 이름을 사용해 의미를 표현할 수 있다.
- 긴 함수를 리팩토링 할 때, 그러한 임시 변수를 함수로 추출하여 분리한다면 뺴낸 함수로 전달해야 할 매개변수를 줄일 수 있다.

<aside>
🎈 임의 변수를 사용하는 것 자체도 리팩토링 기술 중 하나이다.

</aside>

<aside>
🎈 기술 자체는 함수 추출하기와 별반 다를 것이 없다.

</aside>

**아래 코드를 기준으로 접근해보자.**

읽어들인 정보를 바탕으로 Markdown 으로 파일을 생성하는 로직이다.

```java
try (FileWriter fileWriter = new FileWriter("participants.md");
     PrintWriter writer = new PrintWriter(fileWriter)) {
    participants.sort(Comparator.comparing(Participant::username));

    writer.print(header(totalNumberOfEvents, participants.size()));

    participants.forEach(p -> {
        long count = p.homework().values().stream()
                .filter(v -> v == true)
                .count();
        double rate = count * 100 / totalNumberOfEvents;

        String markdownForHomework = String.format("| %s %s | %.2f%% |\n", p.username(), checkMark(p, totalNumberOfEvents), rate);
        writer.print(markdownForHomework);
    });
}
```

```java
String markdownForHomework = String.*format*("| %s %s | %.2f%% |\n", p.username(), checkMark(p, totalNumberOfEvents), rate);
```

→ 어떤가? 한줄로 표현되어 있지만 의미를 파악하기 쉽지 않다.

우선 함수 추출하기를 통해 표현해볼 수 있다.

→ extract method : getMarkdownParticipant

```java
String markdownForHomework = getMarkdownParticipant(totalNumberOfEvents, p, rate);

// 

private String getMarkdownParticipant(int totalNumberOfEvents, Participant p, double rate) {
    String markdownForHomework = String.format("| %s %s | %.2f%% |\n", p.username(), checkMark(p, totalNumberOfEvents), rate);
    return markdownForHomework;
}
```

추출한 함수는 아래와 같고 파라미터가 3개 존재한다.

`getMarkdownParticipant(totalNumberOfEvents, p, rate);`

- 여기서 rate에 대한 부분을 확인하면 아래와 같은 로직으로 계산되고 있다.

```java
long count = p.homework().values().stream()
												          .filter(v -> v == true)
												          .count();
double rate = count * 100 / totalNumberOfEvents;
```

이 또한 함수로 추출할 수 있다.

```java
participants.forEach(p -> {
    double rate = getRate(totalNumberOfEvents, p);
    String markdownForHomework = getMarkdownParticipant(totalNumberOfEvents, p, rate);
    writer.print(markdownForHomework);
});
```

그리고 **getMarkdownParticipant** 함수에서 매개변수로 전달받는 rate 매개변수를 파라미터로 사용하지 않고

메소드 내부로 가져와서 처리할 수 있을 것이다.

```java
participants.forEach(p -> {
    String markdownForHomework = getMarkdownParticipant(totalNumberOfEvents, p);
    writer.print(markdownForHomework);
});
```

```java
private String getMarkdownParticipant(int totalNumberOfEvents, Participant p) {
	return String.format("| %s %s | %.2f%% |\n",
                     p.username(),
                     checkMark(p, totalNumberOfEvents),
                     getRate(totalNumberOfEvents, p));
}
```

<aside>
🎈 기존 전달받는 파라미터로 얻어낼 수 있는 정보라면 
굳이 추가적인 파라미터로 받지 않고 그 해당 사용하는 곳에서 호출하여 사용할 수 있도록 하는 것이다.

</aside>