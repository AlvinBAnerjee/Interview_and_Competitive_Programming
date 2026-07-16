# _11 — Observer

**Type:** Behavioral
**Intent:** Define a one-to-many dependency so that when one object (the
subject) changes state, all its dependents (observers) are notified
automatically. The "publish/subscribe" pattern.

## Standard diagram

```mermaid
classDiagram
    class Subject { <<interface>> +subscribe(Observer) +unsubscribe(Observer) +notifyObservers() }
    class ConcreteSubject { -state }
    class Observer { <<interface>> +update(state) }
    class ConcreteObserver

    Subject <|.. ConcreteSubject
    Observer <|.. ConcreteObserver
    Subject o-- Observer : notifies *
    ConcreteObserver ..> ConcreteSubject : reads state
```

The Subject **holds a list of** Observers and calls `update()` on each when its
state changes — it doesn't know or care what the concrete observers do.

## This repo's example

A `StockTicker` notifies every subscribed `PriceSubscriber` whenever the price
changes; unsubscribing stops the updates.

```mermaid
classDiagram
    class Subject { <<interface>> +subscribe(Observer) +unsubscribe(Observer) +notifyObservers() }
    class StockTicker { -price +setPrice(int) }
    class Observer { <<interface>> +update(int) }
    class PriceSubscriber { -name +update(int) }
    class Client { +main() }

    Subject <|.. StockTicker
    Observer <|.. PriceSubscriber
    StockTicker o-- Observer : notifies *
    Client ..> StockTicker : uses
```

**Roles:** `Subject` = Subject interface · `StockTicker` = ConcreteSubject ·
`Observer` = Observer interface · `PriceSubscriber` = ConcreteObserver ·
`Client` = wires subscriptions.

## Run

```
java MachineCoding_LLD.DesignPatterns._11_ObserverDesignPattern.Client
```
