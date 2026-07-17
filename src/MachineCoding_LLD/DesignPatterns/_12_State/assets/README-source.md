# _12 — State

**Type:** Behavioral
**Intent:** Let an object alter its behavior when its internal state changes —
the object appears to change its class. Each state becomes its own class, so the
context delegates instead of running a big `switch` on a state field.

## Standard diagram

```mermaid
classDiagram
    class Context { -state State +request() }
    class State { <<interface>> +handle(Context) }
    class ConcreteStateA
    class ConcreteStateB

    State <|.. ConcreteStateA
    State <|.. ConcreteStateB
    Context o-- State : current
    ConcreteStateA ..> Context : setState()
    ConcreteStateB ..> Context : setState()
```

The Context forwards each request to its current State object; the State handles
it and may **transition** the Context to a different State.

## This repo's example

A `Turnstile` responds to `coin` and `push`. The same two events behave
differently in each state, and each state flips the turnstile to the other:
`LockedState` + coin → unlocked; `UnlockedState` + push → locked.

```mermaid
classDiagram
    class Turnstile { -state TurnstileState +coin() +push() +setState(TurnstileState) }
    class TurnstileState { <<interface>> +coin(Turnstile) +push(Turnstile) }
    class LockedState
    class UnlockedState
    class Client { +main() }

    TurnstileState <|.. LockedState
    TurnstileState <|.. UnlockedState
    Turnstile o-- TurnstileState : current
    LockedState ..> Turnstile : setState(unlocked)
    UnlockedState ..> Turnstile : setState(locked)
    Client ..> Turnstile : uses
```

**Roles:** `TurnstileState` = State interface · `LockedState`/`UnlockedState`
= ConcreteStates · `Turnstile` = Context · `Client` = drives events.

## Run

```
java MachineCoding_LLD.DesignPatterns._12_State.Client
```
