package MachineCoding_LLD.DesignPatterns._01_FactoryDesignPattern;

public class EmailNotification implements  Notification{
    @Override
    public void sendNotification() {
        System.out.println("Sending Email Notification");
    }
}
