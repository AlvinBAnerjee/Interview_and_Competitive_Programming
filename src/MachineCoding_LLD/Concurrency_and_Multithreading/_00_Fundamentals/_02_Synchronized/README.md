# Lesson 2 — `synchronized` (mutual exclusion)

**Goal:** fix Lesson 1's lost updates so the counter is **always** 200,000.

`synchronized` gives **mutual exclusion**: only **one** thread at a time may hold an
object's intrinsic lock (its *monitor*). Everyone else that hits a `synchronized`
block on the *same object* **waits** until the lock is freed. So `count++`'s three
steps now run as one uninterruptible unit.

> **Bonus:** entering/leaving a `synchronized` block also syncs memory, so it fixes
> **visibility** too. `synchronized` = **atomicity + visibility**.

## Two styles (see [`SynchronizedCounter.java`](./SynchronizedCounter.java))
```java
public synchronized void inc() { count++; }   // locks `this` for the whole method

synchronized (lock) { count++; }               // locks only these lines — more precise
```

## Rules of thumb
- **Keep the critical section small** — lock the shared access, *not* slow I/O.
- Threads must lock the **same** object to exclude each other. Two different locks = no protection.
- It's **reentrant**: a thread already holding a lock can re-acquire it (e.g. a synchronized method calling another).
- Downside: threads **block** and wait. For a *single counter*, a lock-free atomic (Lesson 4) is faster.

## Run it
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_02_Synchronized/SynchronizedCounter.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._02_Synchronized.SynchronizedCounter
```
Correct **every** run now — compare against Lesson 1.

➡️ Next: [Lesson 3 — `volatile`](../_03_Volatile/README.md) tackles the *other* danger: visibility.
