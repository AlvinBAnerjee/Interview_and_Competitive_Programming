# Bounded Blocking Queue — From Scratch  (Medium)

## Problem
Implement a thread-safe bounded blocking queue **without** using `java.util.concurrent`'s
`BlockingQueue`. Support:
- `enqueue(x)` — blocks while full.
- `dequeue()` — blocks while empty, returns the head.
- `size()` — current count.

(LeetCode 1188 "Design Bounded Blocking Queue".)

## Why it's asked
The definitive test of `ReentrantLock` + `Condition`. It forces you to show you understand **two
separate wait conditions** on one lock — which is exactly why `Condition` beats a single
`wait/notify` monitor (you can `signal` only the relevant waiters).

## Design
- One `ReentrantLock`.
- Two `Condition`s: `notFull` (producers wait here) and `notEmpty` (consumers wait here).
- `enqueue`: `while (size == cap) notFull.await();` … add … `notEmpty.signal();`
- `dequeue`: `while (size == 0) notEmpty.await();` … remove … `notFull.signal();`

## Key points to say
- `await()`/`signal()` mirror `wait()`/`notify()` but let you target **the right** waiter set →
  no wasted wakeups, no lost-signal stalls.
- **Always `lock()` outside try, `unlock()` in `finally`.**
- `await()` in a `while` loop (same spurious-wakeup rule).
- Two conditions on one lock is *the* reason to prefer `ReentrantLock` over `synchronized` here.

See [`BoundedBlockingQueue.java`](./BoundedBlockingQueue.java).
