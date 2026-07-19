# Build a Thread Pool From Scratch  (Medium)

## Problem
Implement a fixed-size thread pool **without** `java.util.concurrent`'s `ExecutorService`. Support:
- `submit(Runnable)` — hand a task to the pool.
- `shutdown()` — stop accepting new work, but **finish everything already queued**, then let the
  worker threads die.
- `awaitTermination()` — block until all workers have exited.

You may use a `BlockingQueue` for the task backlog — the exercise is the **worker lifecycle**, not
re-building the queue (that's [`_03_BoundedBlockingQueue`](../_03_BoundedBlockingQueue/PROBLEM.md)).

## Why it's asked
Every Fundamentals lesson *uses* `ExecutorService`; this asks you to *be* it. It shows you
understand what a pool actually is — **N long-lived worker threads draining a shared queue** — and
the genuinely subtle part: **graceful shutdown**. Naively stopping is easy; stopping *after the
backlog drains, with no lost tasks and no thread left blocked forever* is the real question.

## Design
- **Fixed `N` worker threads**, each an infinite loop: take a task off the shared queue, run it,
  repeat.
- **One shared `BlockingQueue<Runnable>`** as the backlog; `take()` blocks a worker when it's empty
  (no busy-wait).
- **`volatile boolean isShutdown`** flag. `submit` rejects once it's set.
- **Graceful drain:** a worker keeps going while `!isShutdown || queue not empty`. To avoid a worker
  parking forever in `take()` after shutdown, it uses **`poll(timeout)`** so it periodically
  re-checks the flag. Loop exits only when shut down **and** the backlog is empty.

## Approaches to shutdown (say the trade-off)
| Approach | How workers stop | Note |
|----------|------------------|------|
| **`poll(timeout)` + flag** (used here) | Wake every T ms, re-check `isShutdown && empty` | Simple, drains cleanly; tiny latency on the flag |
| **Poison pills** | Enqueue N sentinel tasks; a worker that sees one exits | Exact, no polling, but you must inject exactly N |
| **`interrupt()` the workers** | `shutdownNow`-style: drop the backlog immediately | For abrupt stop; workers must handle `InterruptedException` |

## Key points to say
- Workers are created **once** in the constructor and reused — that reuse (vs. `new Thread` per task)
  is the entire point of a pool.
- `take()`/`poll()` **block**, so idle workers cost no CPU — never spin.
- Graceful shutdown = "no new work, drain what's queued, then exit"; the loop condition
  `!isShutdown || !queue.isEmpty()` encodes exactly that.
- `awaitTermination` is just `join()` on every worker.

See [`SimpleThreadPool.java`](./SimpleThreadPool.java).
