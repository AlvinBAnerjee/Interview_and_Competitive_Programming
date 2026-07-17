# Vending Machine  🥤  (Easy)

## Problem Statement
Design a vending machine that accepts coins/notes, lets a user select a product,
dispenses the product and correct change, and handles refunds and out-of-stock items.

## Clarifying Questions to Ask
- Payment: cash only, or card too? Which denominations?
- Should it return change, and can it run out of change?
- One machine, one user at a time, or concurrent?

## Functional Requirements
- Insert money (multiple denominations).
- Select a product → dispense if enough money and in stock.
- Return correct **change**; **refund** on cancel.
- Restock products and coins (admin/maintenance mode).
- Reject invalid selection / insufficient funds / out of stock.

## Non-Functional Requirements
- Robust **state machine** (no dispensing without payment, etc.).
- Extensible to new products and denominations.

## Core Entities
- `VendingMachine` (context), `Inventory`, `Product`, `Coin/Note`
- State: `IdleState`, `HasMoneyState`, `DispenseState`, `OutOfStockState`
- `ChangeStrategy` (greedy change-making)

## APIs
```
void   insertMoney(Denomination d)
Product selectProduct(String code)   // triggers dispense
List<Coin> cancel()                  // refund
void   restock(...)
```

## Design Patterns
- **State** — transitions Idle → HasMoney → Dispense → Idle.
- **Strategy** — change-making algorithm.
- **Factory** — product/coin creation.

## Concurrency / Multithreading  🟢 Low
Classic vending machine is **single-user, single-threaded** — the interviewer usually
wants a clean **State pattern**, not thread-safety. If asked to extend:
- Guard the state transition with a single lock so two users can't both drive the
  machine into `DispenseState` simultaneously.
- Prefer modeling it as **one request at a time** (a lock or a single-consumer queue).
> Use this problem to show mastery of the **State pattern**, not concurrency.

## Evaluation Metrics
- [ ] Correct state machine; illegal transitions rejected.
- [ ] Accurate change-making; handles "can't make change" gracefully.
- [ ] Refund returns exactly what was inserted.
- [ ] Adding a product/denomination doesn't require rewriting states.
- [ ] Clear separation: inventory vs money-handling vs state.
