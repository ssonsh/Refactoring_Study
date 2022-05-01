# 서로 다른 인터페이스의 대안 클래스들

### 냄새 21. 서로 다른 인터페이스의 대안 클래스들

**Alternative Classes with Different Interfaces**

- 비슷한 일을 여러 곳에서 서로 다른 규약을 사용해 지원하고 있는 코드 냄새
- 대안 클래스로 사용하려면 동일 인터페이스를 구현하고 있어야 한다.
- “**함수 선언 변경하기 (Change Function Declaration)**” 와 “**함수 옮기기(Move Function)**”을 사용해서 서로 동일한 인터페이스를 구현하게끔 코드를 수정할 수 있다.
    - [리팩토링 1. 함수 선언 변경하기](https://www.notion.so/1-7c8c6ac9febe45f581c1c872f60f0faa)
    - [리팩토링 25. 함수 옮기기](https://www.notion.so/25-fcf63963c6c44abd8fb6bb48bb90325e)
- 두 클래스에서 일부 코드가 중복되는 경우에 “**슈퍼클래스 추출하기 (Extract Superclass)**”를 사용해 중복된 코드를 슈퍼클래스로 옮기고 두 클래스를 새로운 슈퍼클래스의 서브클래스로 만들 수 있다.
    - [리팩토링 41. 슈퍼클래스 추출하기](https://www.notion.so/41-59ed387b0d5e4ccea200dbdfe5fbe04c)

대안 클래스들 서로 교체해서 쓸 수 있을 것 같은 클래스들을 의미한다.

- 비슷한 역할을 수행하는 클래스들 그러나 서로 호환되지 않는 인터페이스들!?
- ex. JAVA의 DB에 접근하는 기술 jdbc, jpa/hibernate

---

**아래 코드를 보며 살펴보자**

- 알림을 발송하는 서비스가 있고
- Email 발송 혹은 알림 발송이 제공되는 서비스가 있고
- 주문을 수행하면 이 Notification이 처리된다.

AlertService.java

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public interface AlertService {
    void add(AlertMessage alertMessage);
}
```

EmailService.java

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public interface EmailService {
    void sendEmail(EmailMessage emailMessage);
}
```

AlertsService, EmailService 둘다 notification을 발송하는 것은 맞지만 그 방식이 다르다.

또한 발송하는 방법이 다르다.

- AlertsService → add 라는 메소드를 제공
- EmailService → sendEmail 라는 메소드를 제공

이 인터페이스를 원하는대로 뜯어 고칠 수 있겠지만, 고칠 수 없는 경우도 발생한다.

- 고칠 수 없다는 상황으로 가정하고 들어가보자.

[Notification.java](http://Notification.java) 는 아래와 같이 제공되고 있다.

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public class Notification {

    private String title;
    private String receiver;
    private String sender;

    private Notification(String title) {
        this.title = title;
    }

    public static Notification newNotification(String title) {
        return new Notification(title);
    }

    public Notification receiver(String receiver) {
        this.receiver = receiver;
        return this;
    }

    public Notification sender(String sender) {
        this.sender = sender;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }
}
```

Alert과 Email 을 Notification이라는 개념으로 추상화 하여, 추상 계층을 하나 더 만들어낼 수 있을 것이다.

NotificationService.java

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public interface NotificationService {
    void sendNotification(Notification notification);
}
```

- 이 NotificationService 추상 계층을 이용하여 기존의 알림을 위한 서비스를 감싸면
- 두개의 API를 맞춰줄 수 있는 것이다.

먼저 Email을 실제 발송하는 Client 코드를 보자

OrderProcessor.java

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public class OrderProcessor {

    private EmailService emailService;

    public void notifyShipping(Shipping shipping) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTitle(shipping.getOrder() + " is shipped");
        emailMessage.setTo(shipping.getEmail());
        emailMessage.setFrom("no-reply@whiteship.com");
        emailService.sendEmail(emailMessage);
    }

}
```

- EmailService를 사용하고 있는 상태이다.
- 이를 NotificationService를 이용하도록 리팩토링 해보자.



```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public class OrderProcessor {

    private NotificationService notificationService;

    public OrderProcessor(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void notifyShipping(Shipping shipping) {
        Notification notification = Notification.newNotification(shipping.getOrder() + " is shipped")
                                                .receiver(shipping.getEmail())
                                                .sender("no-reply@whiteship.com");
        notificationService.sendNotification(notification);
    }
}
```

Shipping이라는 객체를 이용하여 Notification 객체를 생성하였고

emailService API를 이용하지 않고 추상화된 notificationService를 이용하였다.

추상화 된 NotificationService를 이용하여 Email 발송을 위한 EmailNotificationService 구현체를 만들어낼 수 있다.

EmailNotificationService.java

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public class EmailNotificationService implements NotificationService{

    private EmailService emailService;

    @Override
    public void sendNotification(Notification notification) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTitle(notification.getTitle());
        emailMessage.setTo(notification.getReceiver());
        emailMessage.setFrom("no-reply@whiteship.com");
        emailService.sendEmail(emailMessage);
    }
}
```

비슷하게 AlertsService 쪽도 리팩토링 해보자.

기존 AlertsService를 이용하는 코드는 아래와 같다.

OrderAlerts.java

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public class OrderAlerts {

    private AlertService alertService;
    
    public void alertShipped(Order order) {
        AlertMessage alertMessage = new AlertMessage();
        alertMessage.setMessage(order.toString() + " is shipped");
        alertMessage.setFor(order.getEmail());
        alertService.add(alertMessage);
    }
}
```

아래와 같이 리팩토링 될 수 있다.

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public class OrderAlerts {

    private NotificationService notificationService;

    public OrderAlerts(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void alertShipped(Order order) {
        Notification notification = Notification.newNotification(order.toString() + "is shipped")
                                                .receiver(order.getEmail());
        notificationService.sendNotification(notification);
    }

}
```

NotificationService를 구현한 AlertNotificationService는 아래와 같이 구성될 수 있다.

AlertNotificationService.java

```java
package com.ssonsh.refactoringstudy._21_alternative_classes_with_different_interfaces;

public class AlertsNotificationService implements NotificationService{

    private AlertService alertService;

    @Override
    public void sendNotification(Notification notification) {

        AlertMessage alertMessage = new AlertMessage();
        alertMessage.setMessage(notification.getTitle());
        alertMessage.setFor(notification.getReceiver());
        alertService.add(alertMessage);
    }
}
```


    💡 지금까지 살펴본 방법은 AlertService와 EmailService 각각 직접 수정하기 힘들기 떄문에 각 서비스는 그대로 유지하면서 추상화 계층을 하나 더 정의하여 그 추상화된 계층을 통해 처리하는 방식으로 대응하였다.
     - 이런 부분은 레거시 코드의 리팩토링이 쉽지 않은 경우
     - 쉽게 접근하여 추상화할 수 있는 방법 중 하나이다.




