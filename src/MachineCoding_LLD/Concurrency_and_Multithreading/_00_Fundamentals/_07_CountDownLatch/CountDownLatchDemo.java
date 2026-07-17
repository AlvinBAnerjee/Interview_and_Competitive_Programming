package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._07_CountDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * LESSON 7 — CountDownLatch: wait for N events, exactly once.
 *
 * 3 "worker" threads each do a bit of setup work at their own pace, then countDown().
 * `main` blocks on await() until the count reaches zero — i.e. until ALL workers are
 * ready — and only then proceeds. Once tripped, the latch stays open forever; there is
 * no reset() (see Lesson 8's CyclicBarrier for the reusable version of this idea).
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        int workerCount = 3;
        CountDownLatch readyLatch = new CountDownLatch(workerCount);

        for (int i = 1; i <= workerCount; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    Thread.sleep(100L * id); // simulate uneven startup work
                    System.out.println("worker-" + id + ": ready");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    readyLatch.countDown(); // always count down, even on failure, or main hangs forever
                }
            }).start();
        }

        System.out.println("main: waiting for all workers...");
        readyLatch.await(); // blocks until the count reaches 0
        System.out.println("main: all " + workerCount + " workers ready — GO!");
    }
}
