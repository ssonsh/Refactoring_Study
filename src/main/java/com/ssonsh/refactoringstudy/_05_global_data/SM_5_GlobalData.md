# 냄새 5. 전역 데이터

### 전역 데이터 Global Data

- 전역 데이터 (예, 자바의 public static 변수)
- 전역 데이터는 아무곳에서나 변경될 수 있다는 문제가 있다.
    - 이런 문제로 보통 **“상수”**로 사용하게 된다.
    - **“상수”** 로 사용한다면 큰 문제는 없다. 변경하지 못하기 때문이다.
- 어떤 코드로 인해 값이 바뀐 것인지 파악하기 어렵다.
- **클래스 변수(필드)**도 비슷한 문제를 겪을 수 있다.
- **“변수 캡슐화하기(Encapsulate Variable)”** 을 적용하여 접근을 제어하거나 어디서 사용하는지 파악하기 쉽게 만들 수 있다.
- 파라켈수스의 격언, **`“약과 독의 차이를 결정하는 것은 사용량일 뿐이다.”`**

    <aside>
    🎈 전역 변수가 무조건 나쁘다는 것이 아니다.
    필요에 의해 선언하고 사용할 수 있겠지만, 파라켈수스의 격언에서 볼 수 있듯이 

  이렇게 선언된 전역변수가 사용되는 사용량에 따라 독에 가까워 질 수 있다.

    </aside>