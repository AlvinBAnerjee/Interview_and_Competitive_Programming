# Lesson 12 — `ScheduledExecutorService`

**Goal:** run a task **later**, or **repeatedly on a schedule**.

A plain pool runs a task as soon as a worker is free. `ScheduledExecutorService` adds **time**:
*"run this in 2 seconds"* or *"run this every 500 ms"*. It's the modern replacement for
`java.util.Timer` (which used one thread, and let a single failing task kill all future runs).

Build one with `Executors.newScheduledThreadPool(n)`.

## The three methods

| Method | Fires |
|--------|-------|
| `schedule(task, delay, unit)` | **once**, after `delay`. |
| `scheduleAtFixedRate(task, initialDelay, period, unit)` | repeatedly — each run `period` after the previous **start**. |
| `scheduleWithFixedDelay(task, initialDelay, delay, unit)` | repeatedly — each run `delay` after the previous **finish**. |

## `atFixedRate` vs `withFixedDelay` — the classic distinction

```
fixedRate  (start → start):   |--task--|   |--task--|   |--task--|      steady beat, ignores duration
                              |<-period->|<-period->|

fixedDelay (end → start):     |--task--|___|--task--|___|--task--|      constant GAP between runs
                                        gap         gap
```

- **`fixedRate`** keeps a steady cadence. ⚠️ If a run takes **longer** than `period`, the next
  starts immediately and runs can **pile up**. Good for *sampling on a steady beat*.
- **`fixedDelay`** always leaves a fixed gap; total cadence drifts with task duration. Good for
  *polling* where you want breathing room between calls.

> **Memory hook:** fixed-**RATE** = start→start; fixed-**DELAY** = end→start.

A repeating task **never lets the pool terminate on its own** — you must `cancel()` the
`ScheduledFuture` and/or `shutdown()` the scheduler.

## Run it
```bash
# from the src/ directory
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_12_ScheduledExecutor/ScheduledExecutorDemo.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._12_ScheduledExecutor.ScheduledExecutorDemo
```
You'll see a one-shot fire after ~1s and a fixed-rate "heartbeat" tick ~every 500 ms until it's
cancelled after ~5 ticks.

➡️ Next: [Lesson 13 — `CompletableFuture`](../_13_CompletableFuture/README.md) (composing async work without blocking on `get()`).
