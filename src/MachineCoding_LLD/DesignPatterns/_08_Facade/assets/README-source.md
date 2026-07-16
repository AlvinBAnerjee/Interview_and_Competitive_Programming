# _8 — Facade

**Type:** Structural
**Intent:** Provide one simplified entry point over a complicated subsystem, so
clients don't have to understand or wire up all the moving parts.

## Standard diagram

```mermaid
classDiagram
    class Facade { +doSomething() }
    class SubsystemA
    class SubsystemB
    class SubsystemC
    class Client

    Client ..> Facade : uses
    Facade ..> SubsystemA
    Facade ..> SubsystemB
    Facade ..> SubsystemC
```

The Facade **depends on** the subsystems and hides them; the Client depends only
on the Facade.

## This repo's example

`RestaurantFacade.getMenuFor(preference)` hides the choice between the veg,
non-veg, and mixed restaurants behind one call.

```mermaid
classDiagram
    class RestaurantFacade { +getMenuFor(String) Menus }
    class Hotel { <<interface>> +getMenus() Menus }
    class VegRestaurant
    class NonVegRestaurant
    class VegNonBothRestaurant
    class Client { +main() }

    Hotel <|.. VegRestaurant
    Hotel <|.. NonVegRestaurant
    Hotel <|.. VegNonBothRestaurant
    RestaurantFacade ..> Hotel : delegates
    Client ..> RestaurantFacade : uses
```

**Roles:** `RestaurantFacade` = Facade · `VegRestaurant`/`NonVegRestaurant`/
`VegNonBothRestaurant` (behind `Hotel`) = Subsystem · `Client` = Client.

## Run

```
java MachineCoding_LLD.DesignPatterns._08_Facade.Client
```
