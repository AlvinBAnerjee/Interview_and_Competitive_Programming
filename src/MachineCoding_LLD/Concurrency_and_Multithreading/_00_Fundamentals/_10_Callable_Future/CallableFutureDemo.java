package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._10_Callable_Future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * LESSON 10 — Callable & Future: getting a RESULT (or exception) back out of a task.
 *
 * Runnable.run() returns void — great for fire-and-forget, useless if you need an answer.
 *   Callable<V>.call() RETURNS a value of type V and is allowed to THROW a checked exception.
 *
 * When you submit(Callable) to a pool you immediately get a Future<V> — a HANDLE to a result
 * that may not exist yet. The task runs on a worker thread; your thread keeps going.
 *
 * Future's four essential methods:
 *   - get()               -> BLOCKS until the result is ready, then returns it.
 *   - get(timeout, unit)  -> blocks at most `timeout`, else throws TimeoutException.
 *   - isDone()            -> non-blocking peek: has it finished (normally, by exception, or cancel)?
 *   - cancel(mayInterrupt)-> try to cancel; if it hadn't started it never will, if running and
 *                            mayInterrupt=true the worker is interrupted.
 *
 * THE EXCEPTION MODEL (interview favourite):
 *   get() does NOT throw just because the result "isn't ready" — it waits. It throws:
 *     - ExecutionException  -> the task's call() threw. The REAL cause is e.getCause().
 *     - InterruptedException-> YOUR waiting thread was interrupted while blocked.
 *     - TimeoutException    -> only from get(timeout, unit).
 */
public class CallableFutureDemo {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // 1) A Callable that returns a value after some work.
        Callable<Integer> answer = () -> {
            Thread.sleep(500);
            return 6 * 7;
        };
        Future<Integer> future = pool.submit(answer);

        // The submit() call returned INSTANTLY — the work runs on a worker while we do this:
        System.out.println("Submitted. isDone right away? " + future.isDone()); // almost certainly false
        System.out.println("Doing other work on main() while it computes...");

        try {
            Integer result = future.get(2, TimeUnit.SECONDS); // BLOCKS here until ready (or 2s)
            System.out.println("Result = " + result + " | isDone now? " + future.isDone());
        } catch (TimeoutException e) {
            System.out.println("Timed out waiting for the result.");
        } catch (ExecutionException e) {
            System.out.println("Task threw: " + e.getCause()); // unwrap the real exception
        }

        // 2) A Callable that FAILS — see how the exception surfaces at get(), not at submit().
        Future<Integer> boom = pool.submit(() -> { throw new IllegalStateException("kaboom"); });
        try {
            boom.get();
        } catch (ExecutionException e) {
            System.out.println("As expected, get() reported the failure via getCause(): " + e.getCause());
        }

        // 3) Cancelling a task that hasn't produced a result yet.
        Future<Integer> slow = pool.submit(() -> { Thread.sleep(10_000); return 1; });
        boolean cancelled = slow.cancel(true); // interrupt it if running
        System.out.println("Cancelled the slow task? " + cancelled + " | isCancelled=" + slow.isCancelled());

        pool.shutdown();
    }
}
