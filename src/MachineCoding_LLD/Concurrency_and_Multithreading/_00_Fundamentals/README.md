# Fundamentals — Java Concurrency from Zero

> **New to threads? Start here.** These lessons are meant to be read **in order**. Each
> folder teaches **one** concept with a tiny, runnable example and its own README. Later
> lessons build on earlier ones, so don't skip ahead.

Once these click, move on to [`../NOTES.md`](../NOTES.md) (the cheat sheet),
[`../INTERVIEW_QnA.md`](../INTERVIEW_QnA.md), and the [`../_04_SolvedProblems/`](../_04_SolvedProblems/).

---

## The learning path

| # | Lesson | The one idea | Fixes |
|---|--------|--------------|-------|
| 0 | [Thread Creation](./_00_ThreadCreation/README.md) | 4 ways to start a thread; `start()` vs `run()` vs `join()` | — |
| 1 | [Race Condition](./_01_RaceCondition/README.md) | Why `count++` breaks across threads (the **problem**) | *demonstrates the bug* |
| 2 | [`synchronized`](./_02_Synchronized/README.md) | Mutual exclusion: one thread in the critical section at a time | atomicity **+** visibility |
| 3 | [`volatile`](./_03_Volatile/README.md) | Visibility: always read/write from main memory | visibility only |
| 4 | [`AtomicInteger`](./_04_AtomicInteger/README.md) | Lock-free counting via CAS | atomicity, no lock |
| 5 | [`wait` / `notify`](./_05_WaitNotify/README.md) | Threads **coordinate** — wait for a condition, signal it | coordination |

## The mental model these build toward

Two separate dangers with shared mutable state — **keep them apart**:

- **Atomicity** — a read-modify-write (`count++`) gets interrupted → **Lesson 1**.
  Fix with `synchronized` (L2) or atomics (L4).
- **Visibility** — one thread's write is never *seen* by another → **Lesson 3**.
  Fix with `volatile` or `synchronized`.

| I need to… | Reach for |
|------------|-----------|
| Run work off the main thread | `Thread` / `Runnable` / `ExecutorService` (L0) |
| Guard a multi-step update to shared state | `synchronized` (L2) |
| Publish a simple flag/signal to be read | `volatile` (L3) |
| Count / accumulate under contention | `AtomicInteger` (L4) |
| Make a thread wait for another's signal | `wait`/`notify` → later `BlockingQueue`, `Condition`, `CountDownLatch` (L5) |

## How to run any lesson
Each lesson's README has exact commands. From the `src/` directory the pattern is:
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_00_ThreadCreation/ThreadCreation.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._00_ThreadCreation.ThreadCreation
```
Run the race-condition and volatile demos a **few times** — the whole point is that their
(broken) output changes between runs.
