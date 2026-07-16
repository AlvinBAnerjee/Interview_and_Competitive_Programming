# Java Concurrency — Interview Q&A

Tight, say-out-loud answers to the questions interviewers actually ask. Grouped by theme.
Pair with [`NOTES.md`](./NOTES.md).

---

## A. Fundamentals

**Q1. Concurrency vs Multithreading vs Parallelism?**
Concurrency is a *design property* — structuring work as tasks that overlap in time.
Multithreading is one *mechanism* to achieve it — multiple threads sharing a process's memory.
Parallelism is *simultaneous execution* on multiple cores. You can be concurrent without being
parallel (one thread juggling async I/O) and parallel without traditional threads (GPU/SIMD).
*(Full table in [README](./README.md#-headline-concept-concurrency-vs-multithreading).)*

**Q2. Process vs Thread?**
A process has its own address space (isolated, expensive IPC). Threads live inside a process and
share heap + code (cheap communication, but you must synchronize shared mutable state). A crash in
one thread can take the whole process down; processes are isolated.

**Q3. `start()` vs `run()`?**
`start()` creates a new OS thread and the JVM calls `run()` on it → real concurrency. Calling
`run()` directly just executes it on the current thread — no new thread. Calling `start()` twice
throws `IllegalThreadStateException`.

**Q4. `Runnable` vs `Callable`?**
`Runnable.run()` returns void and can't throw checked exceptions. `Callable.call()` returns a value
and can throw. Use `Callable` with `ExecutorService.submit()` → get a `Future`.

**Q5. What thread states exist?**
`NEW`, `RUNNABLE`, `BLOCKED` (waiting for a monitor lock), `WAITING` (`wait()`/`join()`/`park()`),
`TIMED_WAITING` (`sleep`, timed wait), `TERMINATED`.

---

## B. Memory model & keywords

**Q6. What does `volatile` do — and not do?**
It guarantees **visibility** (writes are flushed and reads see the latest value) and prevents
**reordering** across the access (happens-before). It does **not** make compound actions atomic —
`volatileCount++` is still a race. Use it for flags / single-writer publish, not for counters.

**Q7. Classic use of `volatile`?**
A stop flag: `while (!stopped) {...}` where another thread sets `volatile boolean stopped = true`.
Without `volatile`, the reader may loop forever on a cached value. Also the `volatile` field in
double-checked-locking singletons.

**Q8. What is happens-before?**
The ordering guarantee that makes one thread's memory writes visible to another. Established by
monitor unlock→lock, volatile write→read, `Thread.start()`, `join()`, and `final`-field publication.
If there's no happens-before edge, there's no visibility guarantee.

**Q9. `synchronized` — what does it actually give you?**
Two things: **mutual exclusion** (one thread in the block per monitor) and **visibility**
(happens-before between unlock and the next lock of the same monitor). It's **reentrant**.

**Q10. Why must `wait()`/`notify()` be inside `synchronized`?**
Because they operate on the object's monitor and there's an inherent check-then-act on a shared
condition; holding the lock avoids the lost-wakeup race. Calling them without the monitor throws
`IllegalMonitorStateException`.

**Q11. Why `wait()` in a `while` loop, not `if`?**
Spurious wakeups are allowed, and the condition may have changed by the time you re-acquire the
lock (another thread got there first). Re-check the predicate in a loop.

**Q12. `notify()` vs `notifyAll()`?**
`notify()` wakes one arbitrary waiter; risky if waiters wait on different conditions (lost-wakeup /
deadlock). `notifyAll()` wakes all — safer default. Prefer `notifyAll()` unless you can prove all
waiters are interchangeable.

---

## C. Locks & atomics

**Q13. `synchronized` vs `ReentrantLock`?**
`ReentrantLock` adds: `tryLock()` / timed lock, `lockInterruptibly()`, **fairness** policy, and
**multiple `Condition`s** per lock. Cost: you must `unlock()` in `finally`. Default to
`synchronized` (simpler, JVM-optimized); use `ReentrantLock` when you need those features.

**Q14. How does CAS work? What's the ABA problem?**
Compare-and-swap atomically sets a value only if it still equals the expected value — a lock-free
hardware primitive; you retry in a loop on failure. ABA: value changes A→B→A, so CAS succeeds
though state was mutated in between. Fix with `AtomicStampedReference` (adds a version stamp).

**Q15. `AtomicLong` vs `LongAdder`?**
`AtomicLong` is a single CAS'd cell — contention causes retries. `LongAdder` stripes across
multiple cells to reduce contention, summing on read. Use `LongAdder` for hot write-mostly
counters; `AtomicLong` when you need the exact current value cheaply/often.

**Q16. `ReadWriteLock` — when and what's the catch?**
Read-heavy shared data: many readers concurrently, writers exclusive. Catch: potential **writer
starvation**, and it's only a win when reads dominate and are non-trivial. `StampedLock` offers
faster optimistic reads but isn't reentrant.

---

## D. Executors

**Q17. Why not just `new Thread()` per task?**
Unbounded thread creation → context-switching overhead, memory blowup, OOM. Thread pools reuse a
bounded set of threads, add queueing, backpressure, and lifecycle management.

**Q18. Walk through `ThreadPoolExecutor` sizing.**
Tasks fill up to `corePoolSize` threads → extra tasks go to the `workQueue` → if the queue is full,
grow up to `maximumPoolSize` → if still saturated, the `RejectedExecutionHandler` fires. Idle
non-core threads die after `keepAliveTime`.

**Q19. Why avoid `Executors.newFixedThreadPool` / `newCachedThreadPool` in prod?**
Fixed/single use an **unbounded `LinkedBlockingQueue`** → memory blowup under load. Cached can
create **unbounded threads**. Construct `ThreadPoolExecutor` with a **bounded queue** and an
explicit rejection policy instead.

**Q20. `submit()` vs `execute()`?**
`execute(Runnable)` returns void; an uncaught exception goes to the thread's
`UncaughtExceptionHandler`. `submit()` returns a `Future` and **captures the exception** — you
only see it when you call `future.get()` (which rethrows as `ExecutionException`). A common bug:
exceptions silently swallowed because nobody calls `get()`.

**Q21. How do you gracefully shut down a pool?**
`shutdown()` (stop accepting, finish queued) → `awaitTermination(timeout)` → if it times out,
`shutdownNow()` (interrupt running tasks, return the un-run ones). Handle `InterruptedException`.

**Q22. `CompletableFuture` — how do you combine async calls?**
`thenApply` (transform result), `thenCompose` (chain another async call, flatMap), `thenCombine`
(join two independent futures), `allOf`/`anyOf` (fan-in). Handle errors with `exceptionally` or
`handle`. Pass an explicit `Executor` rather than relying on the common pool for blocking work.

---

## E. Collections & coordination

**Q23. Why is `ConcurrentHashMap.get()` + separate `put()` unsafe for compound logic?**
Each call is individually thread-safe, but the *sequence* isn't atomic — another thread can slip
between them (check-then-act race). Use `computeIfAbsent` / `compute` / `merge` / `putIfAbsent`,
which do the whole operation atomically per key.

**Q24. `CountDownLatch` vs `CyclicBarrier`?**
Latch = one-shot "wait until N events happened" (can't be reset); typical: main thread awaits N
workers. Barrier = reusable "N threads wait for each other" at a rendezvous, with an optional
barrier action; auto-resets each cycle.

**Q25. When `CopyOnWriteArrayList`?**
Read-dominated, write-rare collections (e.g. event-listener lists). Iteration is snapshot-safe and
lock-free; writes copy the whole backing array (expensive) — bad for write-heavy use.

**Q26. `ThreadLocal` — use and gotcha?**
Per-thread storage (e.g. `SimpleDateFormat`, per-request context). Gotcha: in a **thread pool** the
thread outlives the task, so values persist / leak. Always `remove()` in a `finally` block.

---

## F. Problems & debugging

**Q27. What is deadlock and how do you prevent it?**
Four conditions: mutual exclusion, hold-and-wait, no preemption, circular wait — break any one.
Practically: acquire locks in a **global consistent order**, or use `tryLock(timeout)` and back off.

**Q28. Deadlock vs livelock vs starvation?**
Deadlock: threads blocked forever waiting on each other. Livelock: threads keep changing state in
response to each other but make no progress. Starvation: a thread is perpetually denied a resource
(fix with fairness).

**Q29. How do you find a deadlock in production?**
Take a thread dump (`jstack <pid>` or `kill -3`) — the JVM often prints "Found one Java-level
deadlock". Programmatically: `ThreadMXBean.findDeadlockedThreads()`.

**Q30. How do you test concurrent code?**
Force contention: many threads + `CountDownLatch` to release them simultaneously; assert invariants
(size ≤ capacity, no lost updates, no NPE). Run repeatedly / long. Tools: `jcstress` for JMM-level
tests, stress loops, thread sanitizers. Note bugs are non-deterministic — passing once proves little.

**Q31. What are virtual threads (Java 21) and when to use them?**
Lightweight threads scheduled by the JVM onto a small pool of carrier threads; blocking I/O parks
the virtual thread and frees the carrier. Ideal for high-concurrency, I/O-bound servers ("thread
per request" at massive scale). Watch out for **pinning** inside `synchronized`/native code — prefer
`ReentrantLock` there.
