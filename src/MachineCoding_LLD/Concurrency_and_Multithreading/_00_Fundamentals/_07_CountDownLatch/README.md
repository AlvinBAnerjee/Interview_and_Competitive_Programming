# Lesson 7 — `CountDownLatch` (wait for N events, once)

**Goal:** make a thread wait until **N** other threads finish something — then go,
exactly once. No reset, no reuse.

Demo: [`CountDownLatchDemo.java`](./CountDownLatchDemo.java) starts 3 "worker" threads
that each do a bit of setup work and call `countDown()`. `main` blocks on `await()`
until all three are done, then proceeds.

## The pieces
| Method | Effect |
|--------|--------|
| `new CountDownLatch(n)` | Create a gate that opens after `n` calls to `countDown()`. |
| `countDown()` | Decrement the count. Safe to call from any thread; extra calls past 0 are a no-op. |
| `await()` | Block until the count reaches 0. (`await(timeout, unit)` for a bounded wait.) |

## vs `wait`/`notify` (Lesson 5)
A `CountDownLatch` is what you'd otherwise hand-roll with `wait`/`notify` and a shared
counter — minus the bugs. It's simpler because there's exactly one direction (workers →
waiter) and one event (count hits zero).

## One-shot — the key limitation
Once the count reaches 0 it **stays** 0 forever; `await()` returns immediately for every
future caller, and there's no `reset()`. Need a **reusable** gate — e.g. "sync N threads
at a checkpoint, then do it again next round"? That's `CyclicBarrier`, Lesson 8.

## Rules of thumb
- Always `countDown()` in a `finally` block — if a worker throws before counting down,
  every waiter blocks forever.
- Great for "wait for N services to start" / "wait for N tasks to finish" style startup
  and shutdown coordination.

## Run it
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_07_CountDownLatch/CountDownLatchDemo.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._07_CountDownLatch.CountDownLatchDemo
```
`main` prints "waiting..." first, then each worker reports ready (order varies with
sleep time), then "all 3 workers ready — GO!" only after the last one finishes.

➡️ Next: [Lesson 8 — `CyclicBarrier`](../_08_CyclicBarrier/README.md): a **reusable**
rendezvous point where threads wait on *each other*, not on an external signaler.
