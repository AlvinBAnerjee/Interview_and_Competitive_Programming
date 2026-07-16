# Lesson 0 — Creating & Starting Threads

**Goal:** get comfortable spawning a thread and waiting for it.

A **thread** is an independent path of execution inside your program. `main()` itself
runs on a thread (the "main" thread); starting more threads lets work happen *at the same time*.

## The four ways (see [`ThreadCreation.java`](./ThreadCreation.java))

| # | How | When you'd use it |
|---|-----|-------------------|
| 1 | `extends Thread` | Rarely — it "uses up" your only superclass. |
| 2 | `implements Runnable` | Classic, clean. Task is separate from the thread that runs it. |
| 3 | `Runnable` as a **lambda** | Most common today. `Runnable` is a functional interface. |
| 4 | `Callable` + `ExecutorService` | **Production.** Returns a value, reuses threads via a pool. |

## The three methods you must know
- **`start()`** — spawns a *new* thread, which then runs `run()`.
- **`run()`** — ⚠️ calling this *directly* is just a normal method call on the **current** thread — **no concurrency**. Classic trap.
- **`join()`** — "wait here until that thread finishes." Without it, `main()` may exit first.

## Run it
```bash
# from the src/ directory
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_00_ThreadCreation/ThreadCreation.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._00_ThreadCreation.ThreadCreation
```
Notice the lines can print in a **different order each run** — that's the scheduler deciding, and it's your first taste of why concurrency is hard.

➡️ Next: [Lesson 1 — the Race Condition](../_01_RaceCondition/README.md) (why we need everything that follows).
