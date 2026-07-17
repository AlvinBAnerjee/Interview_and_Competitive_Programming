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
| 6 | [`ReentrantLock`](./_06_ReentrantLock/README.md) | Explicit locking: `tryLock`, timeouts, interruptibility, fairness | everything `synchronized` gives, plus non-blocking attempts |
| 7 | [`CountDownLatch`](./_07_CountDownLatch/README.md) | Wait for **N** threads to finish, once (one-shot gate) | "wait for N events" coordination |
| 8 | [`CyclicBarrier`](./_08_CyclicBarrier/README.md) | Reusable rendezvous: **N** threads wait for **each other**, repeatedly | "sync N threads at a checkpoint" coordination |

### Part 2 — The Executor framework (managing threads, not spawning them)

> Lessons 0–8 use raw `Thread`s to *understand* the mechanics. In real code you almost never
> `new Thread()` — you hand **tasks** to a **pool**. These lessons are that production toolkit.

| # | Lesson | The one idea | Fixes |
|---|--------|--------------|-------|
| 9 | [`ExecutorService`](./_09_ExecutorService/README.md) | Pool + reuse worker threads; factory types; shutdown lifecycle | never `new Thread()` per task |
| 10 | [`Callable` / `Future`](./_10_Callable_Future/README.md) | Get a **result** (or exception) back; `get()` blocks; cancel/timeout | task **returns a value** |
| 11 | [`invokeAll` / `invokeAny`](./_11_InvokeAll_InvokeAny/README.md) | Submit a **batch**: all results, or the first winner | fan-out / racing work |
| 12 | [`ScheduledExecutorService`](./_12_ScheduledExecutor/README.md) | Run **later** or **repeatedly**; `atFixedRate` vs `withFixedDelay` | delayed & periodic tasks |
| 13 | [`CompletableFuture`](./_13_CompletableFuture/README.md) | **Compose** async steps without blocking on `get()` | async pipelines |

## The mental model these build toward

Two separate dangers with shared mutable state — **keep them apart**:

- **Atomicity** — a read-modify-write (`count++`) gets interrupted → **Lesson 1**.
  Fix with `synchronized` (L2) or atomics (L4).
- **Visibility** — one thread's write is never *seen* by another → **Lesson 3**.
  Fix with `volatile` or `synchronized`.

| I need to… | Reach for |
|------------|-----------|
| Run work off the main thread | `Thread` / `Runnable` (L0), then `ExecutorService` (L9) in real code |
| Get a **result** back from a task | `Callable` + `Future` (L10) |
| Fan out a batch of tasks | `invokeAll` / `invokeAny` (L11) |
| Run something later / on a timer | `ScheduledExecutorService` (L12) |
| Chain async steps without blocking | `CompletableFuture` (L13) |
| Guard a multi-step update to shared state | `synchronized` (L2), or `ReentrantLock` (L6) for `tryLock`/timeouts |
| Publish a simple flag/signal to be read | `volatile` (L3) |
| Count / accumulate under contention | `AtomicInteger` (L4) |
| Make a thread wait for another's signal | `wait`/`notify` (L5) → later `BlockingQueue`, `Condition` |
| Wait for N threads to finish, once | `CountDownLatch` (L7) |
| Sync N threads at a checkpoint, repeatedly | `CyclicBarrier` (L8) |

## How to run any lesson
Each lesson's README has exact commands. From the `src/` directory the pattern is:
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_00_ThreadCreation/ThreadCreation.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._00_ThreadCreation.ThreadCreation
```
Run the race-condition and volatile demos a **few times** — the whole point is that their
(broken) output changes between runs.
