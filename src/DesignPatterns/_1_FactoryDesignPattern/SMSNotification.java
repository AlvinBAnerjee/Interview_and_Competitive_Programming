package DesignPatterns._1_FactoryDesignPattern;

public class SMSNotification implements  Notification{
    @Override
    public void sendNotification() {
        System.out.println("Sending SMS Notification");
    }
}
