# Lesson 13 — `CompletableFuture`

**Goal:** compose several async steps into a pipeline **without blocking** on `get()` at each stage.

## The noob-friendly version

Imagine ordering pizza the normal way:

```
call the shop -> wait -> wait -> wait -> pizza arrives -> now eat
```

Your whole thread just sits there doing nothing until the pizza shows up. In code:

```java
String pizza = orderPizza(); // blocks — this line doesn't return for 5 seconds
eat(pizza);
```

That's **blocking**. It's fine for one call, but it falls apart once you have several
*independent* things to fetch. Say a login needs the user, their permissions, their
profile picture, and some recommendations — 200ms + 50ms + 500ms + 300ms done one after
another is over a second. But none of those calls depend on each other, so doing them
**in parallel** takes only as long as the slowest one (500ms here) instead of the sum.

`CompletableFuture` is how you get that parallelism without manually juggling threads.
Instead of "wait for the result, then use it," you say "here's what to do *once* the
result shows up" — and the calling thread never has to sit and wait:

```java
CompletableFuture.supplyAsync(this::fetchData)
                  .thenAccept(this::process);
```

Read it as: *fetch data asynchronously → when it's done → process it.* The main thread
kicks this off and moves on immediately; a worker thread runs `fetchData()` and, once
it has a result, runs `process(result)`.

**Before `CompletableFuture`:** you'd create a raw `Thread` per task (expensive, hard to
coordinate) or use a `Future` (Lesson 10) — but a plain `Future` is passive, so your only
option is `get()`, which **blocks**. You can't say "when this finishes, do that next," so
chaining steps still means blocking at every stage. `CompletableFuture` is a `Future` you
attach **callbacks** to instead — the pipeline runs on worker threads (from the shared
common `ForkJoinPool`, or an `Executor` you supply) and the caller never blocks until the
very end, if at all.

**Running several things in parallel and waiting for all of them:**

```java
CompletableFuture<User>  user  = CompletableFuture.supplyAsync(this::fetchUser);
CompletableFuture<Orders> orders = CompletableFuture.supplyAsync(this::fetchOrders);
CompletableFuture<Wallet> wallet = CompletableFuture.supplyAsync(this::fetchWallet);

CompletableFuture.allOf(user, orders, wallet).join(); // wait for all three

Dashboard d = new Dashboard(user.join(), orders.join(), wallet.join());
```

All three calls start at roughly the same time instead of one after another — the total
time is about `max(fetchUser, fetchOrders, fetchWallet)`, not the sum.

**Mental model:** stop asking *"is it done yet?"* and start describing *"what should
happen when it's done."*

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
