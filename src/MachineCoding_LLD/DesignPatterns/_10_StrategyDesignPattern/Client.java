package MachineCoding_LLD.DesignPatterns._10_StrategyDesignPattern;

public class Client {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(500);
        cart.addItem(250);

        cart.setPaymentStrategy(new CreditCardPayment("1234567890123456"));
        cart.checkout();

        cart.setPaymentStrategy(new UpiPayment("alvin@upi"));
        cart.checkout();

        cart.setPaymentStrategy(new PayPalPayment("alvin@example.com"));
        cart.checkout();
    }
}
