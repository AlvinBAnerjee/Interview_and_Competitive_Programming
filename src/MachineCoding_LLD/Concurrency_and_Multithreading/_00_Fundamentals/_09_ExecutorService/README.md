# Lesson 9 — `ExecutorService` & Thread Pools

**Goal:** stop writing `new Thread()` and start handing *tasks* to a reusable pool.

Creating a `Thread` maps to an OS thread with its own stack — it's **expensive**. If 10,000
tasks arrive, spawning 10,000 threads thrashes the machine. A **thread pool** creates a small,
fixed set of worker threads *once*, then feeds them tasks from a queue; each worker **reuses**
its thread for the next task.

You program to the [`ExecutorService`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ExecutorService.html)
interface; `Executors` is the factory that builds one.

## The factory methods to know

| Factory | Threads | Use it when |
|---------|---------|-------------|
| `newFixedThreadPool(n)` | exactly **n** | You want a hard cap; extra tasks wait in an (unbounded) queue. |
| `newCachedThreadPool()` | grows/shrinks | Many **short** tasks. ⚠️ unbounded — a burst can spawn thousands. |
| `newSingleThreadExecutor()` | **1** | Tasks must run one-at-a-time, in submit order. |
| `newVirtualThreadPerTaskExecutor()` *(Java 21+)* | 1 **virtual** thread/task | Massively concurrent blocking I/O, cheaply. |

## `submit` vs `execute`
- **`execute(Runnable)`** — fire-and-forget, returns `void`.
- **`submit(Runnable | Callable)`** — returns a **`Future`** (next lesson) so you can get the
  result, check completion, or cancel.

## The shutdown lifecycle (memorise — common follow-up)
1. **`shutdown()`** — stop accepting new tasks; let queued + running ones finish.
2. **`awaitTermination(t, unit)`** — block up to `t`; returns `false` if it didn't drain in time.
3. **`shutdownNow()`** — best-effort: interrupt running tasks, drop the queue.

> Forget to shut a pool down and its non-daemon worker threads keep the **JVM from exiting**.

## Run it
```bash
# from the src/ directory
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_09_ExecutorService/ExecutorServiceDemo.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._09_ExecutorService.ExecutorServiceDemo
```
Six tasks, a pool of three — notice only **three** distinct `pool-1-thread-N` names appear, and
tasks 4–6 don't start until a worker frees up. That's reuse + queueing in action.

➡️ Next: [Lesson 10 — `Callable` & `Future`](../_10_Callable_Future/README.md) (getting a *result* back out of a task).
