package MachineCoding_LLD.DesignPatterns._10_StrategyDesignPattern;

public class CreditCardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using credit card ending " + cardNumber.substring(cardNumber.length() - 4));
    }
}
