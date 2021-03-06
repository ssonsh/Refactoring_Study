# Refactoring_Study

# Notion Link
https://five-cosmos-fb9.notion.site/Refactoring-843bd25c3aeb42529c714b58f361f4c2


# Refactoring

<aside>
🎈 인프런 백기선님의 “코딩으로 학습하는 리팩토링” 강의를 수강하며 정리해나가는 공간입니다.

[https://www.inflearn.com/course/리팩토링](https://www.inflearn.com/course/%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81)

</aside>

### 리팩토링 (Refactoring)

소프트웨어는 계속 변하기 때문에...

- 처음부터 완벽하게 시스템 설계를 하는 일은 매우 어려운 일이다.
- 이미 코드를 작성한 이후에 구조를 변경하는 일이 발생된다.
- 리팩토링으로 애플리케이션 구조를 꾸준히 개선해나가야 한다.
- 구조 변경으로 인한 버그를 줄이면서 코드를 깔끔하게 유지할 수 있는 방법이다.

**냄새**로 부터 시작하여 어떤 리팩토링 기술을 활용하여 개선시켜 나가볼까?!

라는 접근법으로 진행!

- 냄새에 집중하고!
- 냄새 위주로 접근!

---

### 카탈로그 1. 기본 기술
**가장 자주 사용하는 리팩토링 기술**
- 함수 추출하기
- 함수 인라인 하기
- 변수 추출하기
- 변수 인라인하기
- 함수 선언 변경하기
- 변수 캡슐화하기
- 함수 이름 바꾸기
- 매개변수 객체 만들기
- 여러 함수를 클래스로 묶기
- 여러 함수를 변환 함수로 묶기
- 단계 쪼개기

### 카탈로그 2. 캡슐화
**모듈에서 외부 시스템으로 공개하지 않아도 되는 데이터를 숨기는 기술**
_(모듈은 클래스 일 수 있고 패키지일 수 있고 메소드 일 수 있다)_
- 레코드 캡슐화하기
- 컬렉션 캡슐화하기
- 기본형을 객체로 바꾸기
- 임시 변수를 질의 함수로 바꾸기
- 클래스 추출하기
- 클래스 인라인하기
- 위임 숨기기
- 중재자 제거하기
- 알고리듬 교체하기

### 카탈로그 3. 기능 옮기기
**함수나 필드 또는 문장을 적절한 위치로 옮기는 기술**
- 함수 옮기기
- 필드 옮기기
- 문장을 함수로 옮기기
- 문장을 호출한 곳으로 옮기기
- 인라인 코드를 함수 호출로 바꾸기
- 문장 슬라이드 하기
- 반복문 쪼개기
- 반복문을 파이프라인으로 바꾸기
- 죽은 코드 제거하기

### 카탈로그 4. 데이터 조직화
**데이터 구조를 다루는 기술**
- 변수 쪼개기
- 필드 이름 바꾸기
- 파생 변수를 질의 함수로 바꾸기
- 참조를 값으로 바꾸기 
  - ex. Employee 매개변수 대신에 Employee.getName() 을 주자!
- 값을 참조로 바꾸기
  - ex. 반대로 Employee.getName(), Employee.getPosition() 매개변수 보다는 Employee를 주자!

### 카탈로그 5. 조건부 로직 간소화
**복잡한 조건문을 다루는 기술**
- 조건문 본해하기
- 조건식 통합하기
- 중첩 조건문을 보호 구문으로 바꾸기
- 조건부 로직을 다형성으로 바꾸기
- 특이 케이스 추가하기
- 어서션 추가하기


### 카탈로그 6. API 리팩토링
**쉽고 이해하고 사용할 수 있는 API를 만드는 기술**
- 질의 함수와 변경 함수 분리하기
- 함수 매개변수화 하기
- 플래그 인수 제거하기
- 객체 통째로 넘기기
- 매개변수를 질의 함수로 바꾸기
- 질의 함수를 매개변수로 바꾸기
- 세터 제거하기
- 생성자를 팩토리 함수로 바꾸기
- 함수를 명령으로 바꾸기
- 명령을 함수로 바꾸기


### 카탈로그7. 상속 다루기
**상속을 제대로 사용하는 기술**
- 메소드 올리기
- 필드 올리기
- 생성자 본문 올리기
- 메서드 내리기
- 필드 내리기
- 타입 코드를 서브클래스로 바꾸기
- 서브 클래스 제거하기
- 슈퍼 클래스 추출하기
- 계층 합치기
- 서브 클래스를 위임으로 바꾸기
- 슈퍼 클래스를 위임으로 바꾸기