# Lesson 4 — `AtomicInteger` (lock-free atomicity)

**Goal:** get Lesson 2's correctness *without* making threads block on a lock.

`synchronized` works but threads **wait** for the lock. For a single counter that's
overkill. `AtomicInteger` gives the same correct 200,000 — **lock-free**. See
[`AtomicCounter.java`](./AtomicCounter.java).

## How: CAS (Compare-And-Swap)
CAS is a single CPU instruction. `incrementAndGet()` is essentially:
```java
do {
    old  = get();                    // e.g. 41
    next = old + 1;                  // 42
} while (!compareAndSet(old, next)); // "set to 42 only if still 41" — atomically
```
If another thread changed the value in between, `compareAndSet` fails and it **retries**
with the fresh value. No thread ever sleeps — that's **non-blocking**.

## Handy methods
`incrementAndGet()` · `getAndIncrement()` · `addAndGet(n)` · `get()` · `set(v)` · `compareAndSet(expected, new)`

## When atomics STOP being enough
> Atomics protect **one** variable.

If you must update **two** fields together as a single invariant (transfer money from
account A to B), one CAS can't — go back to a lock (`synchronized` / `ReentrantLock`).

| Situation | Best tool |
|-----------|-----------|
| One counter/flag, high contention | `AtomicInteger` / `AtomicLong` |
| Several fields updated together | `synchronized` / `ReentrantLock` |
| Just publishing a value to be *read* | `volatile` (Lesson 3) |

## Run it
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_04_AtomicInteger/AtomicCounter.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._04_AtomicInteger.AtomicCounter
```

➡️ Next: [Lesson 5 — `wait` / `notify`](../_05_WaitNotify/README.md): making threads *coordinate*, not just count.
