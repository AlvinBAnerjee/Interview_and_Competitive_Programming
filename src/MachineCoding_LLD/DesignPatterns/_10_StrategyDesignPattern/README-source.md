# _10 — Strategy

**Type:** Behavioral
**Intent:** Define a family of interchangeable algorithms, encapsulate each one,
and let the client pick/swap them **at runtime**. Replaces sprawling
`if/else`/`switch` over behavior.

## Standard diagram

```mermaid
classDiagram
    class Strategy { <<interface>> +execute() }
    class ConcreteStrategyA
    class ConcreteStrategyB
    class Context { -strategy Strategy +setStrategy(Strategy) +doWork() }

    Strategy <|.. ConcreteStrategyA
    Strategy <|.. ConcreteStrategyB
    Context o-- Strategy : delegates
```

The Context **holds** a Strategy and delegates the varying step to it, instead
of hard-coding the algorithm.

## This repo's example

A `ShoppingCart` delegates checkout to whichever `PaymentStrategy` is set, so the
payment method can change per checkout without touching the cart.

```mermaid
classDiagram
    class PaymentStrategy { <<interface>> +pay(int) }
    class CreditCardPayment
    class UpiPayment
    class PayPalPayment
    class ShoppingCart { -paymentStrategy +setPaymentStrategy(PaymentStrategy) +checkout() }
    class Client { +main() }

    PaymentStrategy <|.. CreditCardPayment
    PaymentStrategy <|.. UpiPayment
    PaymentStrategy <|.. PayPalPayment
    ShoppingCart o-- PaymentStrategy : delegates
    Client ..> ShoppingCart : uses
```

**Roles:** `PaymentStrategy` = Strategy · `CreditCard`/`Upi`/`PayPalPayment`
= ConcreteStrategies · `ShoppingCart` = Context · `Client` = configures it.

## Run

```
java MachineCoding_LLD.DesignPatterns._10_StrategyDesignPattern.Client
```
