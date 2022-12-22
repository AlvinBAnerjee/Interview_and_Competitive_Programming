package DesignPatterns._1_FactoryDesignPattern;

import java.util.Scanner;

public class NotificationService {
    public static void main(String[] args) {
        NotificationFactory n=new NotificationFactory();
        Notification x=n.createNotification("SMS");
        x.sendNotification();
    }
}
