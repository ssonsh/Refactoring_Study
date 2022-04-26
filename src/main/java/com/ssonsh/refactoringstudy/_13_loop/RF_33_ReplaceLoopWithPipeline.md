# 리팩토링 33. 반복문을 파이프라인으로 바꾸기

**냄새** : 반복문

→ **“반복문을 파이프라인으로 바꾸기”** 를 통해 리팩토링 할 수 있다.

**반복문을 파이프라인으로 바꾸기 Replace Loop with Pipeline**

- 콜렉션 파이프라인 (자바의 Stream, C#의 LINQ - Language Integrated Query)
- 고전적인 반복문을 파이프라인 오퍼레이션을 사용해 표현하면 코드를 더 명확하게 만들 수 있다.
    - **필터 (filter)**
        - 전달받은 조건의 true에 해당하는 데이터만 다음 오퍼레이션으로 전달.
    - **맵 (map)**
        - 전달받은 함수를 사용해 입력값을 원하는 출력값으로 변환하여 다음 오퍼레이션으로 전달.

---

아래 코드를 확인해보자.

Author.java

```java
package com.ssonsh.refactoringstudy._13_loop._33_replace_loop_with_pipeline;

import java.util.ArrayList;
import java.util.List;

public class Author {

    private String company;

    private String twitterHandle;

    public Author(String company, String twitterHandle) {
        this.company = company;
        this.twitterHandle = twitterHandle;
    }

    static public List<String> TwitterHandles(List<Author> authors, String company) {
        var result = new ArrayList<String> ();
        for (Author a : authors) {
            if (a.company.equals(company)) {
                var handle = a.twitterHandle;
                if (handle != null)
                    result.add(handle);
            }
        }
        return result;
    }

}
```

- authors Collection을 순회하면서 매개변수로 받은 company 와 일치하는지 확인하여 그 author의 twitterHandle 값을 확인하여 null이 아니라면
- result라는 Collection으로 추가하고 있다.

→ 이를 Collection Pipeline을 이용하여 리팩토링 해보자.
```java
static public List<String> TwitterHandles(List<Author> authors, String company) {
    return authors.stream()
                  .filter(author -> author.company.equals(company))
                  .map(author -> author.twitterHandle)
                  .filter(Objects::nonNull)
                  .collect(Collectors.toList());
}
```