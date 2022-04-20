# 리팩토링 13. 조건문을 다형성으로 바꾸기

**냄새** : 긴 함수

→ **“조건문을 다형성으로 바꾸기”** 를 통해 리팩토링 할 수 있다.

### 조건문을 다형성으로 바꾸기 Replace Conditional with Polymorphism

- 여러 타입에 따라 각기 다른 로직을 처리해야 하는경우!
    - **다형성을 적용하여 조건문을 보다 명확히 분리할 수 있다.**
    - 반복되는 switch문을 각기 다른 클래스를 만들어 제거 할 수 있다.
- 공통으로 사용되는 로직은 상위 클래스에 두고 달라지는 부분만 하위클래스에 둠으로써, 달라지는 부분만 강조할 수 있다.
- 모든 조건문을 다형성으로 바꿔야 하는 것은 아니다.

<aside>
🎈 비즈니스 상황에 따라 조건문을 유지할 것인지 다형성을 이용해 클래스화 시킬 것인지 판단이 필요하다.

</aside>

---

아래 코드를 기준으로 확인해보자.

**Client 측 코드**

```java
private void print() throws IOException, InterruptedException {
    checkGithubIssues(getGhRepository());
    new StudyPrinter(this.totalNumberOfEvents, this.participants, PrinterMode.MARKDOWN).execute();
}
```

**StudyPrinter.java**

```java
public void execute() throws IOException {
    switch (printerMode) {
        case CVS -> {
            try (FileWriter fileWriter = new FileWriter("participants.cvs");
                 PrintWriter writer = new PrintWriter(fileWriter)) {
                writer.println(cvsHeader(this.participants.size()));
                this.participants.forEach(p -> {
                    writer.println(getCvsForParticipant(p));
                });
            }
        }
        case CONSOLE -> {
            this.participants.forEach(p -> {
                System.out.printf("%s %s:%s\n", p.username(), checkMark(p), p.getRate(this.totalNumberOfEvents));
            });
        }
        case MARKDOWN -> {
            try (FileWriter fileWriter = new FileWriter("participants.md");
                 PrintWriter writer = new PrintWriter(fileWriter)) {

                writer.print(header(this.participants.size()));

                this.participants.forEach(p -> {
                    String markdownForHomework = getMarkdownForParticipant(p);
                    writer.print(markdownForHomework);
                });
            }
        }
    }
}
```

Client에서 StudyPrinter의 execute 메소드를 호출할 때 **`printMode`** 를 전달하고 있으며

“`**printMode**`” 타입에 따라서 로직이 달라진다.

- CVS, CONSOLE, MARKDOWN

<aside>
🎈 위와 같이 switch 문을 통해 작성된 상태를 보면 코드가 길어진 것을 볼 수 있다.

</aside>

**리팩토링 시작**

**StudyPrinter 자체는 그대로 유지하고 StudyPrinter 를 상속받는 클래스를 만든다.**

- CvsPrinter, ConsolePrinter, MarkdownPrinter
- StudyPrinter에 존재하고 있었던 각 타입별 출력 로직을 생성한 각 상속 클래스들이 오버라이딩한 execute 메소드에서 구현해준다.
- StudyPrinter에서 공유해줘야 할 필드들의 접근 지시자를 변경한다.
    - private → protected

```java
package com.ssonsh.refactoringstudy._03_long_function.replace_conditional_with_pholymorphism;

import java.io.IOException;
import java.util.List;

public class ConsolePrinter extends StudyPrinter {
    public ConsolePrinter(int totalNumberOfEvents,
                          List<Participant> participants,
                          PrinterMode printerMode) {
        super(totalNumberOfEvents, participants, printerMode);
    }

    @Override
    public void execute() throws IOException {
        this.participants.forEach(p -> {
            System.out.printf("%s %s:%s\n", p.username(), checkMark(p), p.getRate(this.totalNumberOfEvents));
        });
    }
}
```

```java
package com.ssonsh.refactoringstudy._03_long_function.replace_conditional_with_pholymorphism;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CvsPrinter extends StudyPrinter{
    public CvsPrinter(int totalNumberOfEvents,
                      List<Participant> participants,
                      PrinterMode printerMode) {
        super(totalNumberOfEvents, participants, printerMode);
    }

    @Override
    public void execute() throws IOException {
        try (FileWriter fileWriter = new FileWriter("participants.cvs");
             PrintWriter writer = new PrintWriter(fileWriter)) {
            writer.println(cvsHeader(this.participants.size()));
            this.participants.forEach(p -> {
                writer.println(getCvsForParticipant(p));
            });
        }
    }

    private String getCvsForParticipant(Participant participant) {
        StringBuilder line = new StringBuilder();
        line.append(participant.username());
        for (int i = 1 ; i <= this.totalNumberOfEvents ; i++) {
            if(participant.homework().containsKey(i) && participant.homework().get(i)) {
                line.append(",O");
            } else {
                line.append(",X");
            }
        }
        line.append(",").append(participant.getRate(this.totalNumberOfEvents));
        return line.toString();
    }

    private String cvsHeader(int totalNumberOfParticipants) {
        StringBuilder header = new StringBuilder(String.format("참여자 (%d),", totalNumberOfParticipants));
        for (int index = 1; index <= this.totalNumberOfEvents; index++) {
            header.append(String.format("%d주차,", index));
        }
        header.append("참석율");
        return header.toString();
    }

}
```

```java
package com.ssonsh.refactoringstudy._03_long_function.replace_conditional_with_pholymorphism;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MarkdownPrinter extends StudyPrinter{
    public MarkdownPrinter(int totalNumberOfEvents,
                           List<Participant> participants,
                           PrinterMode printerMode) {
        super(totalNumberOfEvents, participants, printerMode);
    }

    @Override
    public void execute() throws IOException {
        try (FileWriter fileWriter = new FileWriter("participants.md");
             PrintWriter writer = new PrintWriter(fileWriter)) {

            writer.print(header(this.participants.size()));

            this.participants.forEach(p -> {
                String markdownForHomework = getMarkdownForParticipant(p);
                writer.print(markdownForHomework);
            });
        }
    }
    private String getMarkdownForParticipant(Participant p) {
        return String.format("| %s %s | %.2f%% |\n", p.username(), checkMark(p),
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
}
```

**변경된 StudyPrinter 클래스**

- 각 타입에 따라 처리되어야 할 private 메소드들 또한
- 상속받아 구현한 구현 클래스로 이동시키면서 StudyPrinter 클래스 자체의 코드가 줄어들게 된다.
- 각 구현 클래스들이 구현한 execute() 메소드를 StudyPrinter 에서는 더이상 구현할 필요가 없어지게 되며
- StudyPrinter의 `**execute**`() 메서드에 **`abstract`** 키워드를 추가하여 추상 메소드로 정의시킨다.
    - 자연스럽게 StudyPrinter는 abstract 클래스로 변화한다.


**Client 코드**

```java
private void print() throws IOException, InterruptedException {
      checkGithubIssues(getGhRepository());

      // AS-IS
      // new StudyPrinter(this.totalNumberOfEvents, this.participants, PrinterMode.MARKDOWN).execute();

      // TO-BE
      new MarkdownPrinter(this.totalNumberOfEvents, this.participants);
      // TO-BE
      new CvsPrinter(this.totalNumberOfEvents, this.participants);
      // TO-BE
      new ConsolePrinter(this.totalNumberOfEvents, this.participants);

      
}
```

만약, 타입에 따라 동적으로 Printer 구현체가 달라져야 한다면

Factory 개념을 이용하여 Factory 내에서 전달된 타입에 따라 구현 클래스를 지정하여 처리해낼 수 있을 것이다.