# Design Patterns

Each folder is a self-contained, runnable example of one GoF pattern with its own
`README.md` (standard diagram + how this repo's code maps to it).

**Start with [`_00_UML_Relations`](_00_UML_Relations/README.md)** — it explains the
six arrows every diagram below uses.

| # | Pattern | Type | Example |
|---|---------|------|---------|
| 0 | [UML Relations](_00_UML_Relations/README.md) | — | notation primer |
| 1 | [Factory Method](_01_FactoryDesignPattern/README.md) | Creational | notification-by-type |
| 2 | [Abstract Factory](_02_AbstractFactoryDesignPattern/README.md) | Creational | furniture families |
| 3 | [Singleton](_03_SingletonDesignPattern/README.md) | Creational | holder idiom |
| 4 | [Adapter](_04_Adapter/README.md) | Structural | XML → JSON |
| 5 | [Decorator](_05_Decorator/README.md) | Structural | pizza toppings |
| 6 | [Builder](_06_Builder/README.md) | Creational | house builder |
| 7 | [Chain of Responsibility](_07_ChainOfResponsibility/README.md) | Behavioral | ATM dispenser |
| 8 | [Facade](_08_Facade/README.md) | Structural | restaurant menus |
| 9 | [Proxy](_09_Proxy/README.md) | Structural | internet access guard |
| 10 | [Strategy](_10_StrategyDesignPattern/README.md) | Behavioral | payment methods |
| 11 | [Observer](_11_ObserverDesignPattern/README.md) | Behavioral | stock ticker |

Each folder's `README.md` embeds pre-rendered **PNG** diagrams (`assets/README-1.png` =
standard, `assets/README-2.png` = example) so they display everywhere — GitHub, browsers,
and IntelliJ's Markdown preview — without a Mermaid plugin. The Mermaid source
lives in `assets/README-source.md`; to regenerate after an edit:

```
npx @mermaid-js/mermaid-cli -i assets/README-source.md -o _tmp.md -e png -s 2 -b white
# then rename _tmp-1.png / _tmp-2.png to assets/README-1.png / assets/README-2.png
```
