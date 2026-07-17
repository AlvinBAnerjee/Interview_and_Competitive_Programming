package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._13_CompletableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LESSON 13 — CompletableFuture: compose async work WITHOUT blocking on get().
 *
 * A plain Future (Lesson 10) is passive: your only real option is to call get() and BLOCK.
 * You can't say "when this finishes, do that next" — so chaining several async steps means
 * blocking at every stage. CompletableFuture fixes this: it's a Future you can attach
 * CALLBACKS to, building a PIPELINE that runs on worker threads and never blocks the caller.
 *
 * STARTING one:
 *   supplyAsync(supplier)  -> runs the supplier on a pool, completes with its return value.
 *   runAsync(runnable)     -> same, but no result (CompletableFuture<Void>).
 *   (both take an optional Executor; without one they use the common ForkJoinPool.)
 *
 * CHAINING (each returns a NEW stage, so you keep composing):
 *   thenApply(fn)   -> transform the result T -> U            (like map)
 *   thenCompose(fn) -> chain another CF: T -> CF<U>           (like flatMap; avoids CF<CF<U>>)
 *   thenCombine(other, bi) -> wait for TWO independent CFs, combine their results
 *   thenAccept(c)   -> consume the result, produce nothing (CF<Void>)
 *
 * ERROR HANDLING (exceptions flow DOWN the chain):
 *   exceptionally(fn) -> recover: turn a failure into a fallback value
 *   handle(bi)        -> see (result, error) whether it succeeded or failed
 *
 * COMBINING MANY:
 *   allOf(cfs...) -> completes when ALL complete (returns CF<Void>; join each for results)
 *   anyOf(cfs...) -> completes when the FIRST one completes
 *
 * NOTE: join() is like get() but throws an UNCHECKED CompletionException — handy inside lambdas.
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(4);

        // 1) A pipeline: fetch a user id -> load its name -> greet. No blocking between steps.
        CompletableFuture<String> pipeline =
                CompletableFuture.supplyAsync(() -> { sleep(200); return 42; }, pool) // produce an id
                        .thenApply(id -> "user#" + id)          // transform id -> key   (map)
                        .thenCompose(key -> loadNameAsync(key, pool)) // chain another CF (flatMap)
                        .thenApply(name -> "Hello, " + name + "!");   // final transform

        // 2) Combine TWO independent async calls into one result.
        CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> { sleep(150); return 100; }, pool);
        CompletableFuture<Integer> tax   = CompletableFuture.supplyAsync(() -> { sleep(120); return 18; }, pool);
        CompletableFuture<Integer> total = price.thenCombine(tax, Integer::sum); // waits for both

        // 3) Recover from a failure with a fallback instead of propagating the exception.
        CompletableFuture<Integer> resilient =
                CompletableFuture.<Integer>supplyAsync(() -> { throw new RuntimeException("service down"); }, pool)
                        .exceptionally(ex -> {
                            System.out.println("recovering from: " + ex.getMessage());
                            return -1; // fallback value flows on down the chain
                        });

        // We only block ONCE, at the very end, to print — the composition itself never blocked.
        System.out.println(pipeline.get());
        System.out.println("total (price + tax) = " + total.get());
        System.out.println("resilient result    = " + resilient.get());

        // 4) allOf: fan out, then continue once every branch is done.
        CompletableFuture<Void> all = CompletableFuture.allOf(pipeline, total, resilient);
        all.join(); // unchecked wait
        System.out.println("all branches complete.");

        pool.shutdown();
    }

    /** Simulates an async lookup that itself returns a CompletableFuture. */
    private static CompletableFuture<String> loadNameAsync(String key, ExecutorService pool) {
        return CompletableFuture.supplyAsync(() -> {
            sleep(150);
            return key.equals("user#42") ? "Ada" : "Unknown";
        }, pool);
    }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
