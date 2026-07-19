# Concurrency & Multithreading — Interview Prep

A curated set of **concise notes**, **general Q&A**, and **solved classic problems** for Java
concurrency interviews — structured the same way as the sibling
[`LLD_Interview_Problems`](../LLD_Interview_Problems/README.md) folder.

> Goal: not just "make the threads run", but demonstrate that you understand **visibility vs
> atomicity**, choose the **right primitive**, keep **critical sections minimal**, and can
> reason about **deadlock / starvation / races** out loud.

---

## ⭐ Headline Concept: Concurrency vs Multithreading

> This is the single most common warm-up question. Nail it in 30 seconds.

**Concurrency** is a *property of the design*: structuring a program as multiple independent
tasks that can make progress in overlapping time periods. It's about **dealing with many things
at once** (correctly coordinating them).

**Multithreading** is *one implementation mechanism*: using multiple threads (of the same
process, sharing heap memory) to run those tasks. It's about **doing many things at once** on a
particular runtime.

| | Concurrency | Multithreading | Parallelism |
|---|---|---|---|
| **What it is** | Design property — interleaved progress of tasks | A *mechanism* — multiple threads sharing a process | Simultaneous execution on multiple cores |
| **Question it answers** | *How do tasks coordinate?* | *How do we run tasks?* | *Do tasks literally run at the same instant?* |
| **Needs multiple cores?** | No | No | **Yes** |
| **Can exist without the other?** | Concurrency on 1 thread (async I/O, coroutines) | A single thread isn't concurrency | Parallelism needs ≥2 cores |
| **Example** | Async event loop (Node.js) — 1 thread, many tasks | Java `Thread`s sharing a `HashMap` | 8 threads crunching a matrix on an 8-core CPU |

**One-liner for the interview:**
> "Concurrency is about *structure* — dealing with many tasks that overlap in time. Multithreading
> is one *way to achieve it* using threads that share memory. Parallelism is *simultaneous
> execution* on multiple cores. You can have concurrency without parallelism (one thread juggling
> async I/O), and parallelism without traditional threads (SIMD/GPUs)."

Key nuance interviewers probe:
- **Concurrency ≠ parallelism.** Rob Pike's line: *"Concurrency is about dealing with lots of
  things at once; parallelism is about doing lots of things at once."*
- **Multithreading gives you concurrency, and *if* you have multiple cores, potential parallelism.**
- Threads share heap/metaspace (cheap communication, but data races); **processes** don't share
  memory (isolation, but expensive IPC).

---

## Files in This Folder

| File | What's inside |
|------|---------------|
| [`_00_Fundamentals/`](./_00_Fundamentals/README.md) | 🟢 **Start here if you're new.** Numbered one-concept-at-a-time lessons (threads → `synchronized` → `volatile` → atomics → `wait/notify`), each runnable. |
| [`NOTES.md`](./NOTES.md) | **Concise concept notes** — the full cheat sheet (JMM, `volatile`, locks, atomics, executors, collections, latches). |
| [`INTERVIEW_QnA.md`](./INTERVIEW_QnA.md) | **General question & answer** — the questions interviewers actually ask, with tight model answers. |
| [`_04_SolvedProblems/`](./_04_SolvedProblems/) | **Runnable Java solutions** to classic concurrency problems, each with its own `PROBLEM.md`. |
| `_01_ProducerConsumer/`, `_02_ReaderWriter/`, `_03_CoffeeShop/` | Pre-existing worked examples (Semaphore / BlockingQueue based). |

---

## 🟢 New here? The Fundamentals path

If concurrency is new to you, work through [`_00_Fundamentals/`](./_00_Fundamentals/README.md)
**in order** before the notes or solved problems. Each lesson is one idea + one tiny
runnable program:

| # | Lesson | The one idea |
|---|--------|--------------|
| 0 | [Thread Creation](./_00_Fundamentals/_00_ThreadCreation/README.md) | 4 ways to start a thread; `start()` vs `run()` vs `join()` |
| 1 | [Race Condition](./_00_Fundamentals/_01_RaceCondition/README.md) | Why `count++` breaks across threads (**the problem**) |
| 2 | [`synchronized`](./_00_Fundamentals/_02_Synchronized/README.md) | Mutual exclusion — atomicity **+** visibility |
| 3 | [`volatile`](./_00_Fundamentals/_03_Volatile/README.md) | Visibility — always read/write from main memory |
| 4 | [`AtomicInteger`](./_00_Fundamentals/_04_AtomicInteger/README.md) | Lock-free counting via CAS |
| 5 | [`wait` / `notify`](./_00_Fundamentals/_05_WaitNotify/README.md) | Threads **coordinate** — wait for a condition, signal it |
| 6 | [`ReentrantLock`](./_00_Fundamentals/_06_ReentrantLock/README.md) | Explicit locking — `tryLock`, timeouts, fairness |
| 7 | [`CountDownLatch`](./_00_Fundamentals/_07_CountDownLatch/README.md) | Wait for **N** threads to finish, once |
| 8 | [`CyclicBarrier`](./_00_Fundamentals/_08_CyclicBarrier/README.md) | Reusable rendezvous — N threads wait for each other |
| 9 | [`ExecutorService`](./_00_Fundamentals/_09_ExecutorService/README.md) | Thread pools — stop `new Thread()`, reuse workers |
| 10 | [`Callable` / `Future`](./_00_Fundamentals/_10_Callable_Future/README.md) | Get a **result** back; `get()` blocks; cancel/timeout |
| 11 | [`invokeAll` / `invokeAny`](./_00_Fundamentals/_11_InvokeAll_InvokeAny/README.md) | Submit a **batch** — all results, or the first winner |
| 12 | [`ScheduledExecutorService`](./_00_Fundamentals/_12_ScheduledExecutor/README.md) | Run **later** or **repeatedly** on a timer |
| 13 | [`CompletableFuture`](./_00_Fundamentals/_13_CompletableFuture/README.md) | **Compose** async steps without blocking |

---

## Solved Problems Index

| # | Problem | Difficulty | Core Skill Tested | Primitive(s) Used |
|---|---------|-----------|-------------------|-------------------|
| 1 | [Print in Order (Foobar / ZeroEvenOdd)](./_04_SolvedProblems/_01_PrintInOrder/PROBLEM.md) | Easy | Thread signalling / turn-taking | `synchronized`+`wait/notify`, `Semaphore`, `Lock`+`Condition` |
| 2 | [Producer–Consumer](./_04_SolvedProblems/_02_ProducerConsumer/PROBLEM.md) | Easy | Bounded coordination | `wait/notify`, `BlockingQueue`, `Semaphore` |
| 3 | [Bounded Blocking Queue (from scratch)](./_04_SolvedProblems/_03_BoundedBlockingQueue/PROBLEM.md) | Medium | Build a primitive yourself | `ReentrantLock` + two `Condition`s |
| 4 | [Thread-Safe Singleton](./_04_SolvedProblems/_04_ThreadSafeSingleton/PROBLEM.md) | Easy | Lazy init + visibility | `volatile` + double-checked locking, holder idiom, enum |
| 5 | [Dining Philosophers](./_04_SolvedProblems/_05_DiningPhilosophers/PROBLEM.md) | Medium | Deadlock avoidance | Lock ordering, `tryLock`, `Semaphore` (waiter) |
| 6 | [Rate Limiter (Token Bucket)](./_04_SolvedProblems/_06_RateLimiter/PROBLEM.md) | Medium | Atomic counters under load | `AtomicLong`/CAS, `synchronized` refill |
| 7 | [FizzBuzz Multithreaded](./_04_SolvedProblems/_07_FizzBuzzMultithreaded/PROBLEM.md) | Medium | N-way turn-taking (turn = predicate on shared state) | `Lock` + `Condition`, shared counter |
| 8 | [Building H2O (molecule assembly)](./_04_SolvedProblems/_08_H2OMolecule/PROBLEM.md) | Medium | Group rendezvous — gather 2H+1O per batch | Counting `Semaphore`s + `CyclicBarrier` |
| 9 | [Multithreaded Web Crawler](./_04_SolvedProblems/_09_WebCrawlerMultithreaded/PROBLEM.md) | Medium | Thread pool + concurrent dedup + dynamic termination | `ExecutorService`, `ConcurrentHashMap.newKeySet`, `AtomicInteger` counter |
| 10 | [Thread Pool (from scratch)](./_04_SolvedProblems/_10_CustomThreadPool/PROBLEM.md) | Medium | *Be* the executor — worker lifecycle & graceful shutdown | Worker threads + `BlockingQueue` + `volatile` flag |

> Also relevant (in the LLD folder): [Thread-safe **LRU Cache**](../LLD_Interview_Problems/04_Easy_LRUCache/PROBLEM.md)
> and [**Rate Limiter** LLD spec](../LLD_Interview_Problems/08_Medium_RateLimiter/PROBLEM.md).

---

## How to Use This Folder

0. **New to threads?** Do the [`_00_Fundamentals/`](./_00_Fundamentals/README.md) lessons `_00_`→`_05_` in order first.
1. Read [`NOTES.md`](./NOTES.md) once end-to-end — it's the mental model.
2. Drill [`INTERVIEW_QnA.md`](./INTERVIEW_QnA.md) — practice saying answers *out loud*.
3. For each problem: read `PROBLEM.md`, attempt it, then compare with the solution.
4. For every solution, be able to point at **the exact critical section** and **why the chosen
   primitive is correct** (visibility + mutual exclusion).

## Interview Checklist (say these out loud)
- Identify the **critical section** and keep it minimal.
- Separate **visibility** (`volatile`, happens-before) from **atomicity** (locks, CAS).
- Choose **locking granularity**: global vs striped vs per-entity.
- Prefer **immutability** and **atomics/CAS** over locks where possible.
- Guard against **deadlock** (consistent lock ordering / `tryLock`) and **starvation** (fairness).
- Use **`ExecutorService`/thread pools**, never raw `new Thread()` in production-style answers.
- Always call `wait()` **in a loop** (guard against spurious wakeups).
- Know how to **shut down** a pool: `shutdown()` → `awaitTermination()` → `shutdownNow()`.
