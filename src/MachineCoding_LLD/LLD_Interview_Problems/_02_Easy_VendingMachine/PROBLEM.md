# Vending Machine  🥤  (Easy)

## Problem Statement
Design a vending machine that accepts money, lets a user select a product,
dispenses the product and correct change, and handles refunds and out-of-stock items.

## Clarifying Questions to Ask
- Payment: cash only, or card too? Exact denominations, or a flat amount is fine?
- Should it return change?
- One machine, one user at a time, or concurrent?

## Functional Requirements
- Insert money.
- Select a product → dispense if enough money and in stock.
- Return correct **change**; **refund** on cancel.
- Restock products (admin/maintenance mode).
- Reject invalid selection / insufficient funds / out of stock.

## Non-Functional Requirements
- Robust **state machine** (no dispensing without payment, etc.).
- Extensible to new products.

## Core Entities
- `VendingMachine` (context), `Inventory`, `Product`
- State: `IdleState`, `HasMoneyState`

## APIs
```
boolean insertMoney(int amount)
TransactionResult selectProduct(String code)   // triggers dispense
int    cancel()                                // refund
void   restock(...)
```

## Design Patterns
- **State** — transitions Idle → HasMoney → Idle.

## Concurrency / Multithreading  🟢 Low
Classic vending machine is **single-user, single-threaded** — the interviewer usually
wants a clean **State pattern**, not thread-safety. If asked to extend:
- Guard the whole transaction (state check → guards → commit) with a single lock so
  two users can't interleave and both dispense against the same balance.
- Prefer modeling it as **one request at a time** (a lock or a single-consumer queue).
> Use this problem to show mastery of the **State pattern**, not concurrency.

## Evaluation Metrics
- [ ] Correct state machine; illegal transitions rejected.
- [ ] Correct change; keeps money on any rejection so the user can retry or cancel.
- [ ] Refund returns exactly what was inserted.
- [ ] Adding a product doesn't require rewriting states.
- [ ] Clear separation: inventory vs money-handling vs state.
