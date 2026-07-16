package MachineCoding_LLD.DesignPatterns._1_FactoryDesignPattern;

public class EmailNotification implements  Notification{
    @Override
    public void sendNotification() {
        System.out.println("Sending Email Notification");
    }
}
