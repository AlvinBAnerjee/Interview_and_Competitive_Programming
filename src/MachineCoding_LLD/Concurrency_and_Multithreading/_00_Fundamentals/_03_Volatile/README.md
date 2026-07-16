# Lesson 3 — `volatile` (visibility)

**Goal:** understand the *second* danger — **visibility** — which locks fixed for free
but which `volatile` fixes *cheaply* for simple flags.

Each CPU core can cache a copy of a variable for speed. So a worker thread may keep
reading its **stale cached copy** of a flag forever, never seeing that `main()` changed
it — the program **hangs**. See [`VolatileVisibility.java`](./VolatileVisibility.java).

`volatile` tells the JVM: *always read/write this field from main memory, and don't
reorder around it.* A `volatile` write is guaranteed visible to the next `volatile` read.

## Try the experiment
1. Run it as-is → worker stops promptly.
2. **Delete the `volatile` keyword** and re-run → on many JVMs the worker **hangs forever**
   (the demo force-exits after 2s and tells you).

```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_03_Volatile/VolatileVisibility.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._03_Volatile.VolatileVisibility
```

## ⚠️ The one thing everyone gets wrong
> `volatile` gives **visibility, NOT atomicity**.

`volatileCount++` is **still broken** — it's still read-modify-write (Lesson 1).
Use `volatile` for **flags / signals** (`running`, `initialized`), **not** for counters.
For a shared counter, use `synchronized` (L2) or `AtomicInteger` (L4).

| Need | Use |
|------|-----|
| A one-way flag / stop signal | `volatile boolean` |
| A counter many threads mutate | `AtomicInteger` or `synchronized` |
| A multi-step invariant | `synchronized` / `Lock` |

➡️ Next: [Lesson 4 — `AtomicInteger`](../_04_AtomicInteger/README.md): atomicity *without* a lock.
