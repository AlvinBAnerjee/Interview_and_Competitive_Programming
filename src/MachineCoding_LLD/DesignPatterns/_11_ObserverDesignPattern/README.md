# _11 — Observer

**Type:** Behavioral
**Intent:** Define a one-to-many dependency so that when one object (the
subject) changes state, all its dependents (observers) are notified
automatically. The "publish/subscribe" pattern.

## Standard diagram

<img src="./assets/README-1.png" alt="Standard UML class diagram" width="460">

The Subject **holds a list of** Observers and calls `update()` on each when its
state changes — it doesn't know or care what the concrete observers do.

## This repo's example

A `StockTicker` notifies every subscribed `PriceSubscriber` whenever the price
changes; unsubscribing stops the updates.

<img src="./assets/README-2.png" alt="Example UML class diagram — this repo" width="460">

**Roles:** `Subject` = Subject interface · `StockTicker` = ConcreteSubject ·
`Observer` = Observer interface · `PriceSubscriber` = ConcreteObserver ·
`Client` = wires subscriptions.

## Run

```
java MachineCoding_LLD.DesignPatterns._11_ObserverDesignPattern.Client
```
