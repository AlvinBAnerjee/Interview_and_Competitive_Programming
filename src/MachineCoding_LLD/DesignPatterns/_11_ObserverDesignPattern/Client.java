package MachineCoding_LLD.DesignPatterns._11_ObserverDesignPattern;

public class Client {
    public static void main(String[] args) {
        StockTicker ticker = new StockTicker();

        Observer alice = new PriceSubscriber("Alice");
        Observer bob = new PriceSubscriber("Bob");

        ticker.subscribe(alice);
        ticker.subscribe(bob);
        ticker.setPrice(100);

        ticker.unsubscribe(bob);
        ticker.setPrice(110);
    }
}
