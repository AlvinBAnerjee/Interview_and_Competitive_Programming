package MachineCoding_LLD.DesignPatterns._01_FactoryDesignPattern;

public class PushNotification implements  Notification{
    @Override
    public void sendNotification() {
        System.out.println("Sending Push Notification");
    }
}
