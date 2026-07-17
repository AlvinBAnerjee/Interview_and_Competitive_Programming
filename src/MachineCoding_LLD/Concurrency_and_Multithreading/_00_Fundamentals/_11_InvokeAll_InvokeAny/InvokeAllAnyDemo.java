package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._11_InvokeAll_InvokeAny;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * LESSON 11 — invokeAll / invokeAny: submit a WHOLE BATCH of tasks in one call.
 *
 * In Lesson 10 we submit()'d tasks one at a time. Often you have a COLLECTION of tasks and
 * want either ALL their results, or just the FIRST one that succeeds. ExecutorService gives
 * you two blocking helpers for exactly this:
 *
 *   invokeAll(tasks)
 *     - Runs every task, BLOCKS until ALL of them finish.
 *     - Returns List<Future<T>> in the SAME ORDER as the input tasks.
 *     - Every returned Future is already done; get() won't block. A task that threw will
 *       surface its error when you call that Future's get() (as ExecutionException).
 *     - Use when you need to fan out work and gather every answer (e.g. query N shards).
 *
 *   invokeAny(tasks)
 *     - Runs the tasks, BLOCKS until the FIRST one succeeds, returns THAT result directly (a T).
 *     - Cancels the remaining tasks once it has a winner.
 *     - Throws only if ALL tasks fail. Use for redundant/racing work (e.g. ask 3 mirrors,
 *       take whoever answers first).
 */
public class InvokeAllAnyDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // Three "price lookups" that each take a different amount of time.
        List<Callable<String>> tasks = new ArrayList<>();
        tasks.add(makeTask("Vendor-A", 400, 100));
        tasks.add(makeTask("Vendor-B", 200, 105)); // fastest
        tasks.add(makeTask("Vendor-C", 600, 98));

        // ---- invokeAll: wait for ALL, results in submit order ----
        System.out.println("== invokeAll: collect every quote ==");
        List<Future<String>> results = pool.invokeAll(tasks); // blocks until all 3 are done
        for (Future<String> f : results) {
            System.out.println("  got: " + f.get());          // safe: already done, won't block
        }

        // ---- invokeAny: take the FIRST successful quote, cancel the rest ----
        System.out.println("== invokeAny: take whoever answers first ==");
        String firstWinner = pool.invokeAny(tasks);            // returns the T directly
        System.out.println("  winner: " + firstWinner);        // usually Vendor-B (the fastest)

        pool.shutdown();
    }

    /** A task that sleeps `delayMs` then reports a price — simulating a slow network call. */
    private static Callable<String> makeTask(String vendor, long delayMs, int price) {
        return () -> {
            Thread.sleep(delayMs);
            return vendor + " -> $" + price;
        };
    }
}
