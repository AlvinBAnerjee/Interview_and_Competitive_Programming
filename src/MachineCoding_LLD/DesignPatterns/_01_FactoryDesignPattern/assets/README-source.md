# _1 — Factory Method

**Type:** Creational
**Intent:** Move object creation behind a single method so callers ask for a
product *by type* instead of `new`-ing concrete classes themselves.

## Standard diagram

```mermaid
classDiagram
    class Product { <<interface>> +operation() }
    class ConcreteProductA
    class ConcreteProductB
    class Factory { +createProduct(type) Product }

    Product <|.. ConcreteProductA
    Product <|.. ConcreteProductB
    Factory ..> Product : creates
    Factory ..> ConcreteProductA
    Factory ..> ConcreteProductB
```

## This repo's example

A `NotificationFactory` returns the right `Notification` for a
`NotificationType` enum, and the client only ever talks to the interface.

```mermaid
classDiagram
    class Notification { <<interface>> +sendNotification() }
    class EmailNotification
    class SMSNotification
    class PushNotification
    class NotificationFactory { +createNotification(NotificationType) Notification }
    class NotificationService { +main() }

    Notification <|.. EmailNotification
    Notification <|.. SMSNotification
    Notification <|.. PushNotification
    NotificationFactory ..> Notification : creates
    NotificationService ..> NotificationFactory : uses
```

**Roles:** `Notification` = Product · `EmailNotification`/`SMSNotification`/`PushNotification`
= ConcreteProducts · `NotificationFactory` = Factory · `NotificationService` = Client.

## Run

```
java MachineCoding_LLD.DesignPatterns._01_FactoryDesignPattern.NotificationService
```
