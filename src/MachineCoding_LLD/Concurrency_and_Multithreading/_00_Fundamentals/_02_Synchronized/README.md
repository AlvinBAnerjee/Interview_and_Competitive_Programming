# Lesson 2 — `synchronized` (mutual exclusion)

**Goal:** fix Lesson 1's lost updates so the counter is **always** 200,000.

`synchronized` gives **mutual exclusion**: only **one** thread at a time may hold an
object's intrinsic lock (its *monitor*). Everyone else that hits a `synchronized`
block on the *same object* **waits** until the lock is freed. So `count++`'s three
steps now run as one uninterruptible unit.

> **Bonus:** entering/leaving a `synchronized` block also syncs memory, so it fixes
> **visibility** too. `synchronized` = **atomicity + visibility**.

## ⚠️ But visibility is conditional, not automatic
Unlike `volatile` (every write is visible to every future read, unconditionally),
`synchronized` only guarantees visibility **between threads that lock the *same*
monitor**. The JMM calls this a *happens-before* edge: thread A's release of a lock
happens-before thread B's later acquire of **that same lock** — everything A wrote
before releasing is now guaranteed visible to B.

- Thread A writes inside `synchronized(lock) { x = 5; }`, then thread B reads inside
  `synchronized(lock) { print(x); }` on the **same `lock`** → B is guaranteed to see `5`.
- Thread B reads `x` **without** synchronizing at all → **no guarantee**. It can see a
  stale cached value, exactly like the pre-`volatile` bug in Lesson 3.
- Thread A and thread B synchronize on **different** lock objects → also **no
  guarantee** — there's no happens-before edge between unrelated monitors.

So "does the other thread get the update at once?" — **only if it also synchronizes on
the same lock before reading.** If it peeks at the field unsynchronized, it can get the
stale value forever, same as an unsynchronized read of a non-volatile field.

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
