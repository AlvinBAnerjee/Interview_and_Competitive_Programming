# LLD / Machine Coding Interview Problems

A curated set of **Low-Level Design (LLD) / Machine Coding** problems for interview
preparation, ordered by difficulty. Each folder contains a `PROBLEM.md` with:

- **Problem statement** and clarifying scope
- **Functional & non-functional requirements**
- **Core entities / class design hints**
- **APIs to expose**
- **Evaluation metrics** — exactly what an interviewer scores you on
- **Concurrency / Multithreading notes** — where thread-safety is expected

> Goal: not just "make it work", but demonstrate clean OOP, correct use of design
> patterns, extensibility, and (for several of these) **correct concurrent behavior**.

---

## Problem Index

| # | Problem | Difficulty | Multithreading Relevance | Key Patterns |
|---|---------|-----------|--------------------------|--------------|
| 1 | [Parking Lot System](./_01_Easy_ParkingLotSystem/PROBLEM.md) · ✅ [**Solution**](./_01_Easy_ParkingLotSystem/SOLUTION.md) | Easy | 🟡 Medium — concurrent park/unpark, slot allocation | Strategy, Factory, Singleton |
| 2 | [Vending Machine](./_02_Easy_VendingMachine/PROBLEM.md) · ✅ [**Solution**](./_02_Easy_VendingMachine/SOLUTION.md) | Easy | 🟢 Low — single-user state machine | State, Strategy |
| 3 | [Tic-Tac-Toe](./_03_Easy_TicTacToe/PROBLEM.md) · ✅ [**Solution**](./_03_Easy_TicTacToe/SOLUTION.md) | Easy | 🟢 Low — one game turn-based; 🟡 across many games (actor per game) | Strategy, Factory, Observer |
| 4 | [LRU Cache](./_04_Easy_LRUCache/PROBLEM.md) · ✅ [**Solution**](./_04_Easy_LRUCache/SOLUTION.md) | Easy | 🔴 **High** — thread-safe get/put under contention (lock striping) | Strategy, Proxy, Observer, Factory |
| 5 | [Splitwise](./_05_Medium_Splitwise/PROBLEM.md) · ✅ [**Solution**](./_05_Medium_Splitwise/SOLUTION.md) | Medium | 🟡 Medium — lock-free ledger, conserves to zero; greedy debt simplification | Strategy, Factory, Observer, Facade |
| 6 | [Elevator System](./_06_Medium_ElevatorSystem/PROBLEM.md) · ✅ [**Solution**](./_06_Medium_ElevatorSystem/SOLUTION.md) | Medium | 🔴 **High** — request queue, scheduler thread per car | State, Strategy, Command |
| 7 | [BookMyShow](./_07_Medium_BookMyShow/PROBLEM.md) | Medium | 🔴 **High** — seat locking, no double-booking | Singleton, Strategy, Observer |
| 8 | [Rate Limiter](./_08_Medium_RateLimiter/PROBLEM.md) · ✅ [**Solution**](./_08_Medium_RateLimiter/SOLUTION.md) | Medium | 🔴 **High** — lock-free CAS per client, no global lock | Strategy, Factory, Facade |
| 9 | [Cab Booking System](./_09_Hard_CabBookingSystem/PROBLEM.md) | Hard | 🔴 **High** — driver matching, concurrent ride requests | Strategy, Observer, State |
| 10 | [Stock Exchange / Order Matching](./_10_Hard_StockExchange/PROBLEM.md) | Hard | 🔴 **Critical** — lock-free/low-latency matching engine | Strategy, Observer, Command |

Legend: 🟢 Low · 🟡 Medium · 🔴 High/Critical

---

## Multithreading-Focused Problems (Read This for Concurrency Interviews)

If your target role emphasizes **concurrency, thread-safety, or systems programming**,
prioritize these in order. Each `PROBLEM.md` has a dedicated **Concurrency / Multithreading**
section with the exact race conditions to defend against and the primitives to use.

| Priority | Problem | Core Concurrency Challenge | Java Tools You Should Reach For |
|----------|---------|----------------------------|---------------------------------|
| 1 | **LRU Cache** | Atomic `get`+`put`, evict under contention without corrupting the linked list | `ReentrantReadWriteLock`, `ConcurrentHashMap`, striped locks; compare vs `synchronized` |
| 2 | **BookMyShow** | Prevent double-booking of the same seat; seat-hold expiry | Per-seat/per-show locks, `ConcurrentHashMap`, `ScheduledExecutorService`, optimistic locking (CAS) |
| 3 | **Rate Limiter** | Atomically increment/refill counters across many threads | `AtomicLong`, `LongAdder`, `ReentrantLock`, `ConcurrentHashMap` per-key |
| 4 | **Elevator System** | Producer/consumer request queue, one scheduler thread per elevator | `BlockingQueue`, `ExecutorService`, `PriorityBlockingQueue`, `Condition` |
| 5 | **Stock Exchange** | Order-book integrity + low latency; single-writer matching thread | `Disruptor`/ring buffer concept, `BlockingQueue`, lock-free ideas, single-threaded matcher |
| 6 | **Cab Booking** | Concurrent nearby-driver search + atomic driver assignment | `ConcurrentHashMap`, per-driver locks, CAS on driver state |
| 7 | **Parking Lot** | Concurrent allocation of the nearest free slot | `Semaphore` per floor/type, `AtomicInteger`, concurrent free-slot set |
| 8 | **Splitwise** | Concurrent balance updates staying consistent | Per-user lock ordering (deadlock avoidance), `ConcurrentHashMap` |

### Concurrency checklist to demonstrate in interviews
- Identify the **critical section** and keep it minimal.
- Choose the **right granularity of locking** (global vs striped vs per-entity).
- Prefer **immutable objects** and **atomics/CAS** over locks where possible.
- Handle **deadlock** (consistent lock ordering) and **starvation** (fairness).
- Use **`ExecutorService`/thread pools**, never raw `new Thread()` in prod-style answers.
- Show awareness of **visibility** (`volatile`, happens-before) vs **atomicity**.
- Discuss **testing concurrency**: stress tests, `CountDownLatch` to force contention.

---

## How to Use This Folder

1. Read `PROBLEM.md` and **spend 5 min clarifying scope** (as you would in the interview).
2. Sketch the **class diagram / entities** before writing code.
3. Implement in the same package (add a `solution/` subfolder or `.java` files per problem).
4. Re-read the **Evaluation Metrics** and self-grade honestly.
5. For 🔴 problems, write a **stress test** proving thread-safety.

## General Evaluation Rubric (applies to all)

| Dimension | What interviewers look for |
|-----------|----------------------------|
| Requirements gathering | Asked clarifying questions, stated assumptions |
| OOP & abstraction | Clear entities, encapsulation, no God-classes |
| Design patterns | Right pattern for the right reason (not forced) |
| Extensibility | Easy to add new types/rules without editing core logic (OCP) |
| Correctness | Handles edge cases; APIs behave as specified |
| Concurrency | Thread-safe where required; minimal critical sections; no deadlock |
| Code quality | Readable naming, SOLID, testability |
| Communication | Explained trade-offs out loud |
