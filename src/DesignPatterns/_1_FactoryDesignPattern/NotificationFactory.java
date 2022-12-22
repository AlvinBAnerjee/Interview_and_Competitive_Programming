package DesignPatterns._1_FactoryDesignPattern;

public class NotificationFactory {
    public  Notification createNotification(String type)
    {
        if(type.equals("Email"))
            return new EmailNotification();
        else if(type.equals("SMS"))
            return  new SMSNotification();
        else return  new PushNotification();
    }
}
