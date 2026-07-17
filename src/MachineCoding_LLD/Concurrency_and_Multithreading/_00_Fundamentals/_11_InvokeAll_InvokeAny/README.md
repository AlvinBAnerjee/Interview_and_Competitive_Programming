# Lesson 11 — `invokeAll` / `invokeAny`

**Goal:** submit a **whole collection** of tasks in one call, instead of `submit()`-ing one at a time.

You often have a `List<Callable<T>>` and want either **all** their results, or just the **first**
one that succeeds. `ExecutorService` has two blocking helpers for exactly that.

## `invokeAll(tasks)` — wait for everyone
- Runs every task, **blocks until all finish**.
- Returns `List<Future<T>>` in the **same order** as the input tasks.
- Every returned `Future` is already done, so `get()` won't block. A task that threw surfaces
  its error via that `Future`'s `get()` (as `ExecutionException`).
- **Use when** you fan out and need every answer — e.g. query N shards, merge results.

## `invokeAny(tasks)` — take the first winner
- Runs the tasks, **blocks until the first one succeeds**, returns **that result directly** (a `T`,
  not a `Future`).
- **Cancels the rest** once it has a winner.
- Throws only if **all** tasks fail.
- **Use when** work is redundant/racing — e.g. ask 3 mirrors, take whoever answers first.

| | Returns | Blocks until | On failure |
|---|---|---|---|
| `invokeAll` | `List<Future<T>>` (input order) | **all** done | each failure visible via its `Future.get()` |
| `invokeAny` | `T` (the winning result) | **first** success | throws only if **every** task fails |

## Run it
```bash
# from the src/ directory
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_11_InvokeAll_InvokeAny/InvokeAllAnyDemo.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._11_InvokeAll_InvokeAny.InvokeAllAnyDemo
```
`invokeAll` prints all three vendor quotes in submit order; `invokeAny` prints just the fastest
(`Vendor-B`) and abandons the slower two.

➡️ Next: [Lesson 12 — `ScheduledExecutorService`](../_12_ScheduledExecutor/README.md) (delayed & repeating tasks).
