package MachineCoding_LLD.DesignPatterns._11_ObserverDesignPattern;

import java.util.ArrayList;
import java.util.List;

// Concrete subject: holds state (price) and pushes changes to every
// subscribed observer whenever that state changes.
public class StockTicker implements Subject {

    private final List<Observer> observers = new ArrayList<>();
    private int price;

    @Override
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(price);
        }
    }

    public void setPrice(int price) {
        this.price = price;
        notifyObservers();
    }
}
