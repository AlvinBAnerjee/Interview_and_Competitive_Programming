# Splitwise / Expense Sharing  💸  (Medium)

## Problem Statement
Design an expense-sharing app. Users create groups, add expenses split among members
(equal / exact / percentage), and the system tracks who owes whom. Support settling up
and a **simplify-debts** feature that minimizes the number of transactions.

## Clarifying Questions to Ask
- Split types: equal, exact amounts, percentage, shares?
- Groups, or just pairwise? Do we simplify debts (minimize transactions)?
- Multi-currency? Concurrent expense additions?

## Functional Requirements
- Add users; create groups.
- Add an expense: payer, amount, participants, **split strategy**.
- Query balances: what a user owes / is owed, per group and overall.
- Settle up (record a payment).
- (Bonus) **Simplify debts** to minimize number of settlements.

## Non-Functional Requirements
- Balance math must stay **consistent under concurrent writes**.
- Extensible split strategies.
- Handle rounding (cents must sum exactly to total).

## Core Entities
- `User`, `Group`, `Expense`, `Split` (Equal/Exact/Percent), `Balance`/`BalanceSheet`
- `SplitStrategy`, `DebtSimplifier`

## APIs
```
void addExpense(String groupId, String payerId, double amount,
                SplitType type, Map<String,Double> splitInput)
Map<String,Double> getBalances(String userId)
void settleUp(String from, String to, double amount)
```

## Design Patterns
- **Strategy** — split algorithms.
- **Observer** — notify members on new expense (optional).
- **Factory** — split creation from type.

## Concurrency / Multithreading  🟡 Medium
Race: two expenses touching the **same pair of users** update the same balance entry.
- Store balances in a `ConcurrentHashMap<UserPair, Amount>`; update atomically
  (`merge`/`compute`), never read-modify-write without a lock.
- If locking multiple user balances in one expense, **acquire locks in a consistent order**
  (e.g. sorted by user id) to avoid **deadlock**.
- Keep amounts as `long` cents or `BigDecimal` to avoid float drift under concurrency.
- **Test:** many concurrent `addExpense` calls in a group; assert the sum of all balances
  is zero (conservation) and equals the total spent.

## Evaluation Metrics
- [ ] All split types correct; rounding sums exactly to the total.
- [ ] Balances are consistent and **net to zero** overall.
- [ ] New split type addable without editing core (OCP).
- [ ] Debt simplification reduces transaction count correctly (bonus).
- [ ] Concurrent updates don't lose or double-count expenses.
- [ ] Deadlock-free multi-user updates (consistent lock ordering).
