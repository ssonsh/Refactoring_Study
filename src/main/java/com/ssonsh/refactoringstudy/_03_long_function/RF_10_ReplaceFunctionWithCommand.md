# 리팩토링 10. 함수를 명령으로 바꾸기

**냄새** : 긴 함수

→ “함수를 명령으로 바꾸기” 를 통해 리팩토링 할 수 있다.

### 함수를 명령으로 바꾸기 Replace Function with Command

- 함수를 독립적인 객체인, Command로 만들어 사용할 수 있다.
- Command Pattern 을 적용하면 다음과 같은 장점을 취할 수 있다.
    - 부가적인 기능으로 undo 기능을 만들 수도 있다.
    - 더 복잡한 기능을 구현하는데 필요한 여러 메소드를 추가할 수 있다.
    - 상속이나 템플릿을 활용할 수도 있다.
    - 복잡한 메소드를 여러 메소드나 필드로 활용해 쪼갤 수도 있다.
- 대부분의 경우 “커맨드”보다는 “함수”를 사용하지만, 커맨드 말고 다른 방법이 없는 경우에만 사용한다.

---

적용해볼 수 있는 코드를 확인해보자.

- 필요한 정보를 가져온 후 그 정보를 바탕으로 Markdown 파일을 생성하는 코드이다.
    - 향후 예상되는 기능을 보면
        - md 파일로 추출할 수도 있고
        - console log로 추출할 수도 있고
        - cvs 파일로 추출할 수도 있다.

```java
try (FileWriter fileWriter = new FileWriter("participants.md");
     PrintWriter writer = new PrintWriter(fileWriter)) {
    participants.sort(Comparator.comparing(Participant::username));

    writer.print(header(participants.size()));

    participants.forEach(p -> {
        String markdownForHomework = getMarkdownParticipant(p);
        writer.print(markdownForHomework);
    });
}
```

우선 함수 추출하기를 통해 추출해보자.

```java
//

execute(participants);

//
private void exeute(List<Participant> participants) throws IOException {
  try (FileWriter fileWriter = new FileWriter("participants.md");
       PrintWriter writer = new PrintWriter(fileWriter)) {
      participants.sort(Comparator.comparing(Participant::username));

      writer.print(header(participants.size()));

      participants.forEach(p -> {
          String markdownForHomework = getMarkdownParticipant(p);
          writer.print(markdownForHomework);
      });
  }
}
```

Command Pattern 을 적용하기 위해 클래스로 구성해보자.

[StudyPrinter.java](http://StudyPrinter.java) - 1차

- execute 메소드를 1차적으로 가져와서 public 접근 지시자로 변경해주었고
- execute 메소드 내에서 사용하고 있는 내부 메소드들을 가져올 수 있다.
    - 이렇게 되면 StudyDashboard 에 위치했던 메소드들이 StudyPrinter 로 이동될 수 있는 것을 볼 수 있다.
    - 이렇게 까지 하면 출력을 위해 필요로 하는 기능들이 무엇들이 있는지 파악하기 쉬워지고
    - 필요로 하는 파라미터들이 무엇들이 있는지 명확히 파악할 수 있다.

```java
package com.ssonsh.refactoringstudy._03_long_function;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;

public class StudyPrinter {

    public void exeute(List<Participant> participants) throws IOException {
        try (FileWriter fileWriter = new FileWriter("participants.md");
             PrintWriter writer = new PrintWriter(fileWriter)) {
            participants.sort(Comparator.comparing(Participant::username));

            writer.print(header(participants.size()));

            participants.forEach(p -> {
                String markdownForHomework = getMarkdownParticipant(p);
                writer.print(markdownForHomework);
            });
        }
    }

    private String getMarkdownParticipant(Participant p) {
        return String.format("| %s %s | %.2f%% |\n",
                             p.username(),
                             checkMark(p),
                             p.getRate(this.totalNumberOfEvents));
    }

    /**
     * | 참여자 (420) | 1주차 | 2주차 | 3주차 | 참석율 |
     * | --- | --- | --- | --- | --- |
     */
    private String header(int totalNumberOfParticipants) {
        StringBuilder header = new StringBuilder(String.format("| 참여자 (%d) |", totalNumberOfParticipants));

        for (int index = 1; index <= this.totalNumberOfEvents; index++) {
            header.append(String.format(" %d주차 |", index));
        }
        header.append(" 참석율 |\n");

        header.append("| --- ".repeat(Math.max(0, this.totalNumberOfEvents + 2)));
        header.append("|\n");

        return header.toString();
    }

    /**
     * |:white_check_mark:|:white_check_mark:|:white_check_mark:|:x:|
     */
    private String checkMark(Participant p) {
        StringBuilder line = new StringBuilder();
        for (int i = 1 ; i <= this.totalNumberOfEvents ; i++) {
            if(p.homework().containsKey(i) && p.homework().get(i)) {
                line.append("|:white_check_mark:");
            } else {
                line.append("|:x:");
            }
        }
        return line.toString();
    }
}
```

위 경우 `this.totalNumberOfEvents` 값이 StudyPrinter 객체 내부에 없기 떄문에 컴파일 오류가 발생하게 되는데 이 부분을 해결하기 위해

- StudyPrinter의 필드로 정의한다.
- 추가적으로 메소드에서 전달받는 매개변수도 필드로 추출할 수 있다.

이렇게 추출된 필드들은 생성자로 주입받도록 정의할 수 있다.

[StudyPrinter.java](http://StudyPrinter.java) - 2차

```java
package com.ssonsh.refactoringstudy._03_long_function;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;

public class StudyPrinter {

    private int totalNumberOfEvents;
    private List<Participant> participants;

    public StudyPrinter(int totalNumberOfEvents, List<Participant> participants) {
        this.totalNumberOfEvents = totalNumberOfEvents;
        this.participants = participants;
    }

    public void exeute() throws IOException {
        try (FileWriter fileWriter = new FileWriter("participants.md");
             PrintWriter writer = new PrintWriter(fileWriter)) {
            this.participants.sort(Comparator.comparing(Participant::username));

            writer.print(header(this.participants.size()));

            this.participants.forEach(p -> {
                String markdownForHomework = getMarkdownParticipant(p);
                writer.print(markdownForHomework);
            });
        }
    }

    private String getMarkdownParticipant(Participant p) {
        return String.format("| %s %s | %.2f%% |\n",
                             p.username(),
                             checkMark(p),
                             p.getRate(this.totalNumberOfEvents));
    }

    /**
     * | 참여자 (420) | 1주차 | 2주차 | 3주차 | 참석율 |
     * | --- | --- | --- | --- | --- |
     */
    private String header(int totalNumberOfParticipants) {
        StringBuilder header = new StringBuilder(String.format("| 참여자 (%d) |", totalNumberOfParticipants));

        for (int index = 1; index <= this.totalNumberOfEvents; index++) {
            header.append(String.format(" %d주차 |", index));
        }
        header.append(" 참석율 |\n");

        header.append("| --- ".repeat(Math.max(0, this.totalNumberOfEvents + 2)));
        header.append("|\n");

        return header.toString();
    }

    /**
     * |:white_check_mark:|:white_check_mark:|:white_check_mark:|:x:|
     */
    private String checkMark(Participant p) {
        StringBuilder line = new StringBuilder();
        for (int i = 1; i <= this.totalNumberOfEvents; i++) {
            if (p.homework().containsKey(i) && p.homework().get(i)) {
                line.append("|:white_check_mark:");
            }
            else {
                line.append("|:x:");
            }
        }
        return line.toString();
    }
}
```

**사용 코드**

```java
new StudyPrinter(this.totalNumberOfEvents, participants).exeute();
```

이렇게 추출된 StudyPrinter 는 다음을 위해 확장하기 좋은 상태가 되었다.

Printer 라는 인터페이스를 구현하고

이 인터페이스를 구현한 MarkdownPrinter, CVSPrinter, ConsolePrinter 와 같이 Print를 위한 구체 클래스들을 구현해내고 확장할 수 있게 되었다.