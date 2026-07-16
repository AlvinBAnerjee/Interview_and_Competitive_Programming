# Lesson 5 — `wait()` / `notify()` (coordination)

**Goal:** make one thread **wait** for a condition another thread will create — the
foundation of producer/consumer and every "signal me when ready" pattern.

Demo: a **one-slot mailbox** ([`WaitNotify.java`](./WaitNotify.java)). The consumer
starts first and must `wait()` until the producer `put()`s something.

## The tools (call **only** while holding the object's lock, i.e. inside `synchronized`)
| Method | Effect |
|--------|--------|
| `wait()` | **Release the lock** and sleep until notified. (Efficient — no CPU spinning.) |
| `notify()` | Wake **one** waiting thread. |
| `notifyAll()` | Wake **all** waiting threads (usually the safer default). |

## Two rules you must never break
1. **Only inside `synchronized`** on that same object — otherwise `IllegalMonitorStateException`.
2. **Always `wait()` inside a `while` loop** that re-checks the condition — never an `if`.
   Threads can wake **spuriously**, or another thread may have taken the item first. The
   loop re-checks and goes back to sleep if needed:
   ```java
   while (message != null) {   // NOT if
       lock.wait();
   }
   ```

## Run it
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_05_WaitNotify/WaitNotify.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._05_WaitNotify.WaitNotify
```
Output alternates put/took cleanly — no lost or duplicated messages.

## In real life
You'd usually reach for a `BlockingQueue` (which hides all of this), a `CountDownLatch`,
or a `ReentrantLock` + `Condition`. This lesson shows what's happening **underneath** them.

➡️ You've finished Fundamentals. Now try the [Solved Problems](../../_04_SolvedProblems/) —
start with [Print in Order](../../_04_SolvedProblems/_01_PrintInOrder/PROBLEM.md), which builds directly on this handshake.
