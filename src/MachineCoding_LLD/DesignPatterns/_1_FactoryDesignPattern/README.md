# _1 — Factory Method

**Type:** Creational
**Intent:** Move object creation behind a single method so callers ask for a
product *by type* instead of `new`-ing concrete classes themselves.

## Standard diagram

<img src="./README-1.png" alt="Standard UML class diagram" width="460">

## This repo's example

A `NotificationFactory` returns the right `Notification` for a
`NotificationType` enum, and the client only ever talks to the interface.

<img src="./README-2.png" alt="Example UML class diagram — this repo" width="460">

**Roles:** `Notification` = Product · `EmailNotification`/`SMSNotification`/`PushNotification`
= ConcreteProducts · `NotificationFactory` = Factory · `NotificationService` = Client.

## Run

```
java MachineCoding_LLD.DesignPatterns._1_FactoryDesignPattern.NotificationService
```
