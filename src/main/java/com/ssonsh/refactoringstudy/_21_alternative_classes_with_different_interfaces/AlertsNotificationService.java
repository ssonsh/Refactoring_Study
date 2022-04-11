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
