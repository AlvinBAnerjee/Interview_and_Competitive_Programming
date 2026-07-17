# Lesson 13 — `CompletableFuture`

**Goal:** compose several async steps into a pipeline **without blocking** on `get()` at each stage.

A plain `Future` (Lesson 10) is passive — your only real option is `get()`, which **blocks**. You
can't say *"when this finishes, do that next"*, so chaining steps means blocking at every stage.
`CompletableFuture` is a `Future` you attach **callbacks** to, building a pipeline that runs on
worker threads and never blocks the caller until the very end (if at all).

## Starting one
- `supplyAsync(supplier)` — run on a pool, complete with the returned value.
- `runAsync(runnable)` — same, but no result (`CompletableFuture<Void>`).
- Both take an optional `Executor`; without one they use the shared **common ForkJoinPool**.

## Chaining (each returns a *new* stage)
| Method | Does | Analogy |
|--------|------|---------|
| `thenApply(fn)` | transform `T → U` | `map` |
| `thenCompose(fn)` | chain another CF: `T → CF<U>` | `flatMap` (avoids `CF<CF<U>>`) |
| `thenCombine(other, bi)` | wait for **two** CFs, combine results | zip |
| `thenAccept(c)` | consume the result, return nothing | `forEach` |

## Error handling (exceptions flow **down** the chain)
- `exceptionally(fn)` — recover: turn a failure into a fallback value.
- `handle(bi)` — see `(result, error)` whether it succeeded or failed.

## Combining many
- `allOf(cfs...)` — completes when **all** complete (`CF<Void>`; `join` each for its result).
- `anyOf(cfs...)` — completes when the **first** completes.

> `join()` is like `get()` but throws an **unchecked** `CompletionException` — handy inside lambdas
> where checked exceptions are a pain.

## Run it
```bash
# from the src/ directory
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_13_CompletableFuture/CompletableFutureDemo.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._13_CompletableFuture.CompletableFutureDemo
```
You'll see a 3-step pipeline (`id → key → name → greeting`), two parallel calls combined into a
total, and a failing call recovered via `exceptionally` — all composed without a blocking `get()`
until the final prints.

🎉 That's the Executor-framework track. Back to the [Fundamentals index](../README.md), or on to
the [`../../NOTES.md`](../../NOTES.md) cheat sheet and [`../../_04_SolvedProblems/`](../../_04_SolvedProblems/).
