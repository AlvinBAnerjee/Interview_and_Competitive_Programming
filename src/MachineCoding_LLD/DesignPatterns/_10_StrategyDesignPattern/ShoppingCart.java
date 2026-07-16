package MachineCoding_LLD.DesignPatterns._10_StrategyDesignPattern;

// Context: delegates the checkout algorithm to whichever strategy it holds,
// so the payment method can be swapped at runtime without changing the cart.
public class ShoppingCart {

    private PaymentStrategy paymentStrategy;
    private int total;

    public void addItem(int price) {
        total += price;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void checkout() {
        paymentStrategy.pay(total);
    }
}
