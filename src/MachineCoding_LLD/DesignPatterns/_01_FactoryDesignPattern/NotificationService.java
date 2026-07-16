package MachineCoding_LLD.DesignPatterns._01_FactoryDesignPattern;

public class NotificationService {
    public static void main(String[] args) {
        NotificationFactory factory = new NotificationFactory();
        for (NotificationType type : NotificationType.values()) {
            Notification notification = factory.createNotification(type);
            notification.sendNotification();
        }
    }
}
