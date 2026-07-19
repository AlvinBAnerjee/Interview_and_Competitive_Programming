package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._10_CustomThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A fixed-size thread pool built from scratch (no ExecutorService).
 *
 * The essence of a pool: N long-lived worker threads that reuse themselves to
 * drain a shared task queue. The interesting part is GRACEFUL shutdown --
 * stop accepting work, finish everything already queued, then let workers die
 * with no task lost and no thread parked forever.
 *
 * Worker loop condition:  keep going while  (!isShutdown || queue not empty).
 * Workers use poll(timeout) rather than take() so that, after shutdown, a
 * worker blocked on an empty queue wakes up, re-checks the flag, and exits.
 */
public class SimpleThreadPool {

    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final List<Worker> workers = new ArrayList<>();
    private volatile boolean isShutdown = false;

    public SimpleThreadPool(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            Worker w = new Worker("pool-worker-" + i);
            workers.add(w);
            w.start();                       // workers created ONCE, then reused for every task
        }
    }

    /** Hand a task to the pool. Rejected once shutdown has begun. */
    public void submit(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("pool is shut down; not accepting tasks");
        }
        taskQueue.offer(task);               // wakes a worker parked in poll()
    }

    /** Stop accepting new work; already-queued tasks still run to completion. */
    public void shutdown() {
        isShutdown = true;
    }

    /** Block until every worker has drained the backlog and exited. */
    public void awaitTermination() throws InterruptedException {
        for (Worker w : workers) {
            w.join();
        }
    }

    private class Worker extends Thread {
        Worker(String name) { super(name); }

        @Override
        public void run() {
            // Run while there is work, OR while we are still accepting work.
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    // Timed poll: if shutdown flips while we're idle, we wake and re-check.
                    Runnable task = taskQueue.poll(200, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        task.run();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;                   // treat interrupt as an abrupt stop
                }
            }
        }
    }

    // ---- demo ----
    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool pool = new SimpleThreadPool(3);
        AtomicInteger completed = new AtomicInteger();

        for (int i = 1; i <= 9; i++) {
            final int id = i;
            pool.submit(() -> {
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                System.out.println(Thread.currentThread().getName() + " ran task " + id);
                completed.incrementAndGet();
            });
        }

        pool.shutdown();          // no new tasks accepted, but the 9 queued ones still run
        pool.awaitTermination();  // wait for the backlog to fully drain

        System.out.println("Completed " + completed.get() + " tasks (expected 9).");
    }
}
