# Lesson 6 — `ReentrantLock` (explicit locking)

**Goal:** get everything `synchronized` gives you, plus the things it *can't* do: try
the lock without blocking, wait with a timeout, or respond to interruption.

`ReentrantLock` (`java.util.concurrent.locks`) is an **explicit** lock — you call
`lock()` / `unlock()` yourself instead of the JVM managing it via a `synchronized`
block. See [`ReentrantLockDemo.java`](./ReentrantLockDemo.java).

## Always unlock in `finally`
```java
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();   // MUST run even if the critical section throws
}
```
Unlike `synchronized`, the JVM won't release an explicit lock for you if an exception
escapes — forget the `finally` and the lock leaks forever, and every other thread that
wants it blocks forever too.

## What it adds over `synchronized`
| Method | Effect |
|--------|--------|
| `tryLock()` | Grab the lock **only if free right now** — returns `false` instead of blocking. |
| `tryLock(timeout, unit)` | Wait **up to** a timeout, then give up. |
| `lockInterruptibly()` | Block, but respond to `Thread.interrupt()` instead of ignoring it. |
| `newCondition()` | Multiple wait-queues per lock (like Lesson 5's `wait`/`notify`, but *N* conditions instead of one). |
| `new ReentrantLock(true)` | **Fair** mode — longest-waiting thread goes first (costs throughput). |

## Still reentrant, like `synchronized`
A thread already holding the lock can re-acquire it (e.g. a locked method calling
another locked method on the same lock) without deadlocking itself — hence the name.

## Rules of thumb
- Default to `synchronized` for plain mutual exclusion — simpler, and impossible to
  forget to unlock.
- Reach for `ReentrantLock` when you need `tryLock`, a timeout, interruptibility, or
  multiple conditions.

## Run it
```bash
javac MachineCoding_LLD/Concurrency_and_Multithreading/_00_Fundamentals/_06_ReentrantLock/ReentrantLockDemo.java
java  MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._06_ReentrantLock.ReentrantLockDemo
```
Part 1 prints a correct 200,000. Part 2 prints `tryLock()` returning `false` while
another thread still holds the lock, then `true` once it's released within the timeout.

➡️ Next: [Lesson 7 — `CountDownLatch`](../_07_CountDownLatch/README.md): make threads
wait for a **one-time** event instead of a lock.
