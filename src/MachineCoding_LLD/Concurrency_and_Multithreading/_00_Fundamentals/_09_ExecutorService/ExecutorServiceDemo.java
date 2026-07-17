package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._09_ExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * LESSON 9 — ExecutorService: stop calling `new Thread()`.
 *
 * PROBLEM with raw threads (Lesson 0): creating a Thread is EXPENSIVE (OS thread + stack),
 * and if 10,000 tasks arrive you'd spawn 10,000 threads and thrash the machine.
 *
 * FIX: a THREAD POOL. You create a FIXED set of worker threads once, then hand them
 * *tasks*. Workers pull tasks off a queue and REUSE their thread for the next task.
 * You program against the `ExecutorService` interface; `Executors` is the factory.
 *
 * THE FACTORY METHODS you must know:
 *   - newFixedThreadPool(n)   -> exactly n threads; extra tasks WAIT in an unbounded queue.
 *   - newCachedThreadPool()   -> grows on demand, reuses idle threads, kills them after 60s.
 *                                Great for many short tasks; DANGEROUS for unbounded bursts.
 *   - newSingleThreadExecutor()-> 1 thread; tasks run one-at-a-time, in submit order.
 *   - newVirtualThreadPerTaskExecutor() (Java 21+) -> a cheap virtual thread PER task.
 *
 * submit(...) vs execute(...):
 *   - execute(Runnable) -> fire-and-forget, returns void.
 *   - submit(Runnable/Callable) -> returns a Future (Lesson 10) so you can track/cancel it.
 *
 * THE LIFECYCLE (memorise this 3-step shutdown — a common interview follow-up):
 *   1. shutdown()          -> stop accepting new tasks; let queued+running ones finish.
 *   2. awaitTermination(t) -> block up to t for the pool to drain; returns false on timeout.
 *   3. shutdownNow()       -> best-effort: interrupt running tasks, drop the queue.
 * Forgetting to shut a pool down keeps its (non-daemon) threads alive -> the JVM never exits.
 */
public class ExecutorServiceDemo {

    public static void main(String[] args) throws InterruptedException {
        // A pool of 3 workers. We will throw 6 tasks at it, so tasks 4-6 QUEUE and wait
        // for a free worker. Watch the output: only 3 distinct "pool-1-thread-N" names.
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 6; i++) {
            int taskId = i;
            pool.submit(() -> {                       // Runnable task — fire and forget
                String worker = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " STARTED  on " + worker);
                sleep(300);                           // pretend to do work
                System.out.println("Task " + taskId + " finished on " + worker);
            });
        }

        // Graceful shutdown, the textbook way:
        pool.shutdown();                              // 1. no new tasks accepted from here on
        boolean drained = pool.awaitTermination(5, TimeUnit.SECONDS); // 2. wait for the rest
        if (!drained) {
            pool.shutdownNow();                       // 3. give up waiting; interrupt stragglers
        }

        System.out.println("All tasks done. Pool drained cleanly? " + drained);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();       // restore the flag; never swallow silently
        }
    }
}
