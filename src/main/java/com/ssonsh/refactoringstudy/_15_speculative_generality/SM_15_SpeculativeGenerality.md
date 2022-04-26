### 추측성 일반화 Speculative Generality

- 나중에 이러 저러한 기능이 생길 것으로 예상하여, 여러 경우에 필요로 할만한 기능을 만들어 놨지만 “그런 일은 없었고...” 결국 쓰이지 않는 코드가 발생
- **XP**(익스트림 프로그래밍)의 **YAGNI (You aren`t gonna need it)** 원칙을 따르자
    - *지금 당장 필요한게 아니면 만들지 말라.*
    - 너무 지나치게 생각을 하게 되면서 발생되는 일반화를 경계하자.

**관련 리팩토링**

- 추상 클래스를 만들었지만 크게 유효하지 않다면 “**계층 합치기(Collapse Hierachy)**”
    - [리팩토링 34. 계층 합치기](https://www.notion.so/34-13a3d65f488645eab8a53a7615c82bd1)
- 불필요한 위임은 “**함수 인라인 (Inline Function)**” 또는 “**클래스 인라인(Inline Class)**”
    - [리팩토링 28. 함수 인라인](https://www.notion.so/28-da550ca205754bf5b9665a9e4449f2f5)
    - [리팩토링 29. 클래스 인라인](https://www.notion.so/29-385f639e7e3a40e4a950c511414f196b)
- 사용하지 않는 매개변수를 가진 함수는 “**함수 선언 변경하기(Change Function Declaration)**”
    - [리팩토링 1. 함수 선언 변경하기](https://www.notion.so/1-7c8c6ac9febe45f581c1c872f60f0faa)
- 오로지 테스트 코드에서만 사용하고 있는 코드는 “**죽은 코드 제거하기(Remove Declaration)**”