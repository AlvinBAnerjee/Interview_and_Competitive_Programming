package MachineCoding_LLD.DesignPatterns._11_ObserverDesignPattern;

public class PriceSubscriber implements Observer {
    private final String name;

    public PriceSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void update(int price) {
        System.out.println(name + " notified: new price is " + price);
    }
}
