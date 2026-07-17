package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._08_CyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * LESSON 8 — CyclicBarrier: a reusable rendezvous point.
 *
 * 3 "runners" each race through 2 rounds at their own pace. Within a round, a runner
 * that finishes early must wait at the barrier for the other 2 — nobody starts round 2
 * until ALL THREE have finished round 1. Unlike CountDownLatch, the SAME threads both
 * signal (by arriving) and wait, and the barrier automatically resets after each trip —
 * that's the "cyclic" part.
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        int runnerCount = 3;
        int rounds = 2;

        CyclicBarrier barrier = new CyclicBarrier(runnerCount,
                () -> System.out.println("-- all runners reached the barrier, starting next round --"));

        for (int i = 1; i <= runnerCount; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    for (int round = 1; round <= rounds; round++) {
                        Thread.sleep(100L * id); // uneven pace each round
                        System.out.println("runner-" + id + ": finished round " + round);
                        barrier.await(); // wait here until all 3 have arrived
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
