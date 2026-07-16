package MachineCoding_LLD.DesignPatterns._11_ObserverDesignPattern;

public interface Subject {
    void subscribe(Observer observer);
    void unsubscribe(Observer observer);
    void notifyObservers();
}
