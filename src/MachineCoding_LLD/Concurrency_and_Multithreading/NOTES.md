# Java Concurrency — Concise Notes (Cheat Sheet)

Fast-reference notes. Depth-ordered: fundamentals → memory model → locks → executors →
collections → coordination → pitfalls. Pair with [`INTERVIEW_QnA.md`](./INTERVIEW_QnA.md).

---

## 1. Threads — the basics
- **Thread**: smallest unit of scheduling; threads of one process share **heap + metaspace**, but
  each has its own **stack + program counter**.
- Create via: `extends Thread` · `implements Runnable` (preferred — composition) · `Callable<V>`
  (returns a value / throws, used with `ExecutorService`).
- `start()` spawns a new thread and calls `run()` on it. Calling `run()` directly = **just a
  normal method call on the current thread** (no concurrency). Classic trick question.
- **Lifecycle states**: `NEW → RUNNABLE → (BLOCKED | WAITING | TIMED_WAITING) → TERMINATED`.
- **Daemon thread**: JVM exits when only daemons remain (e.g. GC). Set before `start()`.
- `join()` — wait for a thread to finish. `Thread.sleep()` — TIMED_WAITING, **keeps locks**.
  `yield()` — hint only. Thread priorities are **unreliable** (OS-dependent).

## 2. The core problem: shared mutable state
Two dangers, keep them separate in your head:
- **Race condition / atomicity**: interleaved read-modify-write (`i++` is 3 ops). Fix: mutual
  exclusion (`synchronized`, `Lock`) or atomics/CAS.
- **Visibility**: a write by thread A may never be *seen* by thread B (CPU caches, reordering).
  Fix: `volatile`, `synchronized`, or `final` publication.

## 3. Java Memory Model (JMM) & happens-before
- Compiler/CPU may **reorder** instructions as long as single-thread semantics hold.
- **happens-before** = the guarantee that memory effects of one action are visible to another.
  Established by:
  - Unlock of a monitor **happens-before** a later lock of the same monitor.
  - A `volatile` write **happens-before** every later `volatile` read of that field.
  - `Thread.start()` HB everything the thread does; everything a thread does HB `join()` return.
  - Constructor's `final` field writes HB publication of a properly-constructed object.
- **`volatile`** guarantees *visibility + ordering*, **NOT atomicity** of compound ops (`count++`
  on a volatile is still broken).

## 4. `synchronized`
- Reentrant intrinsic lock on an object's **monitor**. `synchronized method` locks `this`
  (or the `Class` for static). `synchronized(obj){}` locks `obj`.
- Provides **mutual exclusion + visibility** (happens-before on the same monitor).
- `wait()` / `notify()` / `notifyAll()` — **must be called while holding that object's monitor**,
  else `IllegalMonitorStateException`.
  - `wait()` **releases the lock** and parks; re-acquires on wake.
  - **Always `wait()` in a `while` loop**, never `if` — guards against *spurious wakeups* and
    stale conditions. Prefer `notifyAll()` over `notify()` unless you can prove one waiter.

## 5. Explicit locks — `java.util.concurrent.locks`
| Tool | When |
|------|------|
| `ReentrantLock` | Like `synchronized` but with `tryLock()`, `tryLock(timeout)`, `lockInterruptibly()`, and **fairness** option. **Always `unlock()` in `finally`.** |
| `ReentrantReadWriteLock` | Many readers OR one writer. Good for read-heavy data. Beware writer starvation. |
| `StampedLock` | Optimistic reads (`tryOptimisticRead`) — faster than RW lock; **not reentrant**. |
| `Condition` | `lock.newCondition()` → `await()`/`signal()`/`signalAll()`. The `Lock`-world replacement for `wait/notify`; can have **multiple** conditions per lock (e.g. `notFull`, `notEmpty`). |

`synchronized` vs `ReentrantLock`: use `synchronized` by default (simpler, JVM-optimized); reach
for `ReentrantLock` when you need timeout/interruptible/fair/multiple conditions.

## 6. Atomics & CAS
- `AtomicInteger`, `AtomicLong`, `AtomicReference<T>`, `AtomicBoolean`, arrays, field updaters.
- Backed by **CAS (compare-and-swap)**: `compareAndSet(expected, new)` — a lock-free hardware
  primitive. Loop-retry on failure ("optimistic").
- **ABA problem**: value goes A→B→A, CAS can't tell. Fix: `AtomicStampedReference` (version tag).
- **`LongAdder` / `LongAccumulator`**: beat `AtomicLong` under **high write contention** by
  striping counters across cells (sum on read). Use for hot metrics/counters.

## 7. Executors & thread pools
- **Never manage raw threads in prod answers** — use `ExecutorService`.
- `Executors.newFixedThreadPool / newCachedThreadPool / newSingleThreadExecutor /
  newScheduledThreadPool`. ⚠️ Interviewers want: *"prefer constructing `ThreadPoolExecutor`
  directly"* because the factory methods use **unbounded queues** (fixed/single) or **unbounded
  threads** (cached) → OOM risk.
- **`ThreadPoolExecutor` knobs**: `corePoolSize`, `maximumPoolSize`, `keepAliveTime`, `workQueue`
  (bounded!), `ThreadFactory`, **`RejectedExecutionHandler`** (`AbortPolicy` default,
  `CallerRunsPolicy`, `DiscardPolicy`, `DiscardOldestPolicy`).
  - Growth order: fill core → **queue** → grow to max → **reject**.
- `submit()` returns a `Future` (swallows exceptions into the Future); `execute()` returns void
  (exception goes to the thread's `UncaughtExceptionHandler`).
- **`Future`** — `get()` blocks; `get(timeout)`; `cancel()`.
- **`CompletableFuture`** — async composition: `thenApply` (map), `thenCompose` (flatMap),
  `thenCombine` (zip), `allOf`/`anyOf`, `exceptionally`/`handle`. Runs on `ForkJoinPool.commonPool`
  unless you pass an executor.
- **`ForkJoinPool`** — work-stealing; backs parallel streams. Good for divide-and-conquer.
- **Graceful shutdown**: `shutdown()` (no new tasks, drain) → `awaitTermination(t)` →
  `shutdownNow()` (interrupt running).

## 8. Concurrent collections
| Collection | Use / note |
|-----------|-----------|
| `ConcurrentHashMap` | Default concurrent map. Java 8+: CAS + `synchronized` per bin (not the old Java 7 segments). `get` is lock-free. **Compound ops aren't atomic** unless you use `compute` / `computeIfAbsent` / `merge` / `putIfAbsent`. |
| `CopyOnWriteArrayList/Set` | Read-heavy, rare writes (listeners). Every write copies the array. |
| `BlockingQueue` | `ArrayBlockingQueue` (bounded), `LinkedBlockingQueue` (optionally bounded), `PriorityBlockingQueue`, `SynchronousQueue` (0-capacity handoff), `DelayQueue`. `put`/`take` block. The backbone of producer-consumer + thread pools. |
| `ConcurrentLinkedQueue` | Lock-free unbounded FIFO (Michael-Scott). Non-blocking `poll`. |
| `Collections.synchronizedMap` | Coarse single-lock wrapper — worse than `ConcurrentHashMap`; iteration still needs external sync. |

## 9. Coordination utilities
| Tool | One-liner |
|------|-----------|
| `CountDownLatch` | **One-shot** gate. `countDown()` n times; `await()` unblocks. Not reusable. |
| `CyclicBarrier` | **Reusable** rendezvous: N threads `await()` each other; optional barrier action. Resets. |
| `Semaphore` | N permits; `acquire()`/`release()`. Bound concurrency / build mutex (1 permit) / pools. |
| `Phaser` | Flexible, dynamic-party barrier (advanced). |
| `Exchanger` | Two threads swap objects at a sync point. |
| `ThreadLocal` | Per-thread variable. ⚠️ In pools, **`remove()` in `finally`** or you leak memory / bleed state across tasks. |

`CountDownLatch` vs `CyclicBarrier`: latch = *"wait for N events"* (one-time, one waiter can wait
on others); barrier = *"N threads wait for each other"* (reusable, symmetric).

## 10. Deadlock, livelock, starvation
- **Deadlock** needs all 4 (Coffman): mutual exclusion, hold-and-wait, no preemption, circular
  wait. **Break one** → prevent it. Practical fix: **global lock ordering** or `tryLock(timeout)`.
- **Livelock**: threads keep reacting to each other, no progress (two people dodging in a hallway).
- **Starvation**: a thread never gets the resource (fix: fairness).
- **Detect in prod**: thread dump (`jstack`, `kill -3`), JMX `ThreadMXBean.findDeadlockedThreads()`.

## 11. Modern Java (mention if role uses 17/21+)
- **Virtual threads (Loom, Java 21)**: millions of cheap threads; blocking I/O is fine — the
  carrier platform thread is released. Great for high-concurrency I/O servers. Watch **pinning**
  (a virtual thread pinned to its carrier inside `synchronized`/native — prefer `ReentrantLock`).
- **Structured concurrency (`StructuredTaskScope`)**: treat a group of subtasks as a unit
  (all-or-none, propagate cancellation).
- **Records + immutability** make safe sharing trivial (no synchronization needed).

## 12. Quick "which tool?" decision guide
- Just need a visible flag → **`volatile`**.
- Counter under contention → **`AtomicLong`** (or `LongAdder` if very hot).
- Guard a multi-step invariant → **`synchronized` / `ReentrantLock`**.
- Producer/consumer handoff → **`BlockingQueue`**.
- Limit concurrent access to N → **`Semaphore`**.
- Wait for N tasks to finish → **`CountDownLatch`** (or `CompletableFuture.allOf`).
- Share a map safely → **`ConcurrentHashMap`** (+ `compute*` for compound ops).
- Run tasks → **`ExecutorService`** (bounded `ThreadPoolExecutor`).
