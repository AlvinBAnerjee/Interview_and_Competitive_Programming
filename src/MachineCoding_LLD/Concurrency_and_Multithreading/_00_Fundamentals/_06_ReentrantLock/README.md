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

## `Condition` — multiple wait-queues per lock

Every `synchronized` object has exactly **one** wait-queue (Lesson 5's `wait`/`notify`).
`ReentrantLock` lets you carve out **several** wait-queues off the same lock via
`lock.newCondition()` — each one is a `Condition`, and each behaves like its own
mini `wait`/`notify` pair:

| `synchronized` | `Condition` | Effect |
|-----------------|-------------|--------|
| `wait()` | `condition.await()` | Release the lock, sleep until signalled |
| `notify()` | `condition.signal()` | Wake **one** thread waiting on *this* condition |
| `notifyAll()` | `condition.signalAll()` | Wake **all** threads waiting on *this* condition |

You still need the lock held to call any of these — `condition.await()` releases
the lock while parked and re-acquires it before returning, exactly like `wait()`.

**Why bother, if it's the same idea?** Because `notify()` on a plain monitor wakes
*some* waiter, but you don't get to choose *which kind*. In a bounded queue you have
two totally different reasons to wait — "queue is full" (producers) and "queue is
empty" (consumers) — and waking the wrong group is wasted work: a woken producer
just checks `while (full)`, sees it's still full, and goes back to sleep.

`Condition` lets you split the one wait-queue into exactly the groups you need:

```java
private final ReentrantLock lock = new ReentrantLock();
private final Condition notFull  = lock.newCondition(); // producers wait here
private final Condition notEmpty = lock.newCondition(); // consumers wait here

// producer, after adding an element:
notEmpty.signal();   // wakes ONE consumer — never wastes a wakeup on a producer

// consumer, after removing an element:
notFull.signal();    // wakes ONE producer — never wastes a wakeup on a consumer
```

See it end-to-end in
[`BoundedBlockingQueue.java`](../../_04_SolvedProblems/_03_BoundedBlockingQueue/BoundedBlockingQueue.java):
`enqueue()` awaits on `notFull` while the queue is at capacity and signals `notEmpty`
after adding; `dequeue()` awaits on `notEmpty` while empty and signals `notFull` after
removing. Same **while-loop-around-await** rule from Lesson 5 applies (re-check the
condition after waking — never assume the first check that got you into the loop is
still true).

`Condition` also adds `awaitUninterruptibly()`, and a timed
`await(timeout, unit)` — the `Condition` equivalent of `tryLock(timeout, unit)`.

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
