# _2 — Abstract Factory

**Type:** Creational
**Intent:** Create **families of related products** through one factory
interface, guaranteeing the products you get belong to the same variant.
(Factory Method makes one product; Abstract Factory makes a matching set.)

## Standard diagram

```mermaid
classDiagram
    class AbstractFactory { <<interface>> +createA() ProductA +createB() ProductB }
    class ConcreteFactory1
    class ConcreteFactory2
    class ProductA { <<interface>> }
    class ProductB { <<interface>> }

    AbstractFactory <|.. ConcreteFactory1
    AbstractFactory <|.. ConcreteFactory2
    ConcreteFactory1 ..> ProductA : creates
    ConcreteFactory1 ..> ProductB : creates
```

## This repo's example

`FurnitureFactory` produces a `Chair` **and** a `Sofa`; picking
`ModernFurnitureFactory` vs `VictorianFurnitureFactory` swaps the whole style
consistently.

```mermaid
classDiagram
    class FurnitureFactory { <<interface>> +createChair() Chair +createSofa() Sofa }
    class ModernFurnitureFactory
    class VictorianFurnitureFactory
    class Chair { <<interface>> +sitOn() }
    class Sofa { <<interface>> +lieOn() }
    class FurnitureShop { +furnish() }

    FurnitureShop ..> FurnitureFactory : uses
    FurnitureFactory <|.. ModernFurnitureFactory
    FurnitureFactory <|.. VictorianFurnitureFactory
    Chair <|.. ModernChair
    Chair <|.. VictorianChair
    Sofa <|.. ModernSofa
    Sofa <|.. VictorianSofa
    ModernFurnitureFactory ..> ModernChair : creates
    ModernFurnitureFactory ..> ModernSofa : creates
    VictorianFurnitureFactory ..> VictorianSofa : creates
    VictorianFurnitureFactory ..> VictorianChair : creates
```

**Roles:** `FurnitureFactory` = AbstractFactory · `Modern*`/`Victorian*Factory`
= ConcreteFactories · `Chair`,`Sofa` = AbstractProducts · `FurnitureShop` = Client.

## Run

```
java MachineCoding_LLD.DesignPatterns._02_AbstractFactoryDesignPattern.FurnitureShop
```
