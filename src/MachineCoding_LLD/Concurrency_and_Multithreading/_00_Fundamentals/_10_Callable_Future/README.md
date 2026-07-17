# Lesson 10 — `Callable` & `Future`

**Goal:** get a **result** (or exception) back out of a task you handed to a pool.

`Runnable.run()` returns `void` — fine for fire-and-forget, useless when you need an answer.
`Callable<V>.call()` **returns a `V`** and may **throw a checked exception**.

When you `submit(Callable)` you immediately get a **`Future<V>`** — a *handle* to a result that
may not exist yet. The task runs on a worker; your thread keeps going until you actually need
the value.

## `Future`'s four essential methods

| Method | Behaviour |
|--------|-----------|
| `get()` | **Blocks** until the result is ready, then returns it. |
| `get(timeout, unit)` | Blocks at most `timeout`, else throws `TimeoutException`. |
| `isDone()` | Non-blocking peek — finished normally, by exception, **or** cancelled? |
| `cancel(mayInterrupt)` | Try to cancel; if running and `mayInterrupt=true`, interrupt the worker. |

## The exception model (interview favourite)
`get()` does **not** throw just because the result "isn't ready yet" — it *waits*. It throws:

- **`ExecutionException`** — the task's `call()` threw. The real cause is **`e.getCause()`**.
- **`InterruptedException`** — *your* waiting thread was interrupted while blocked.
- **`TimeoutException`** — only from the `get(timeout, unit)` overload.

> Key mental model: the failure of a task is **deferred** to the moment you call `get()`, not the
> moment you `submit()`.

## Run it
```bash
# from the src/ directory
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_10_Callable_Future/CallableFutureDemo.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._10_Callable_Future.CallableFutureDemo
```
Watch three things: `submit()` returns instantly (work happens off-thread), a failing task
surfaces its error only at `get()` via `getCause()`, and a slow task can be `cancel()`led.

➡️ Next: [Lesson 11 — `invokeAll` / `invokeAny`](../_11_InvokeAll_InvokeAny/README.md) (submit a *whole batch* of tasks at once).
