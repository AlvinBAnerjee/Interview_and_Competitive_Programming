package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._00_ThreadCreation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * LESSON 0 — How do I even start a thread?
 *
 * A "thread" is a separate line of execution that runs at the same time as your
 * main() method. Below are the 4 ways you will see in interviews, from oldest to
 * most modern. Read them top to bottom.
 *
 * THE #1 TRICK QUESTION:
 *   - t.start()  -> spawns a NEW thread, then run run() on it   (real concurrency)
 *   - t.run()    -> just a normal method call on the CURRENT thread (NO new thread!)
 *   Always call start(). Calling run() directly is the classic mistake.
 */
public class ThreadCreation {

    // ---- Way 1: extend Thread (rarely preferred — you "use up" your one superclass) ----
    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("[Way 1] extends Thread, running on: " + Thread.currentThread().getName());
        }
    }

    // ---- Way 2: implement Runnable (preferred classic style — composition, no return value) ----
    static class MyTask implements Runnable {
        @Override
        public void run() {
            System.out.println("[Way 2] implements Runnable, running on: " + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Way 1
        Thread t1 = new MyThread();
        t1.start();                 // start() -> new thread. (t1.run() would NOT be concurrent!)

        // Way 2
        Thread t2 = new Thread(new MyTask());
        t2.start();

        // Way 3: Runnable as a lambda (most common in modern code — Runnable is a functional interface)
        Thread t3 = new Thread(() ->
                System.out.println("[Way 3] lambda Runnable, running on: " + Thread.currentThread().getName()));
        t3.start();

        // join() = "wait here until that thread has finished". Without it, main() might
        // print the last line and exit before the threads above even run.
        t1.join();
        t2.join();
        t3.join();

        // Way 4: Callable + ExecutorService — the PRODUCTION way.
        //   * Runnable.run() returns void; Callable.call() RETURNS a value and can throw.
        //   * You submit tasks to a pool and get a Future (a handle to the future result).
        //   * You reuse threads instead of doing `new Thread()` everywhere (which is expensive).
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Callable<Integer> job = () -> {
            System.out.println("[Way 4] Callable, running on: " + Thread.currentThread().getName());
            return 6 * 7;
        };
        Future<Integer> future = pool.submit(job);

        // future.get() does NOT throw just because the task "isn't there yet" — it BLOCKS
        // until the result is ready. The exceptions it can throw are:
        //   - ExecutionException  -> the task itself threw (get its real cause via getCause())
        //   - InterruptedException -> the waiting thread was interrupted while blocked
        //   - TimeoutException    -> only from the get(timeout, unit) overload, if it doesn't
        //                            finish in time
        // To check completion WITHOUT blocking, use future.isDone() / future.isCancelled().
        System.out.println("[Way 4] isDone before get(): " + future.isDone());
        try {
            System.out.println("[Way 4] Callable returned: " + future.get(1, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            System.out.println("[Way 4] task didn't finish in time");
        }

        pool.shutdown(); // ALWAYS shut a pool down, or the JVM will not exit (non-daemon threads).

        System.out.println("main() is done — notice this may not be the last line printed above.");
    }
}
