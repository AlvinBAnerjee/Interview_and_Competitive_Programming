package DesignPatterns._1_FactoryDesignPattern;

public class PushNotification implements  Notification{
    @Override
    public void sendNotification() {
        System.out.println("Sending Push Notification");
    }
}
