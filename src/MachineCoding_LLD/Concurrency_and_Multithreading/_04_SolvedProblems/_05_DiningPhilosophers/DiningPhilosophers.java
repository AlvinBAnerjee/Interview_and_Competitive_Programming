package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._05_DiningPhilosophers;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dining Philosophers, deadlock-free via RESOURCE ORDERING:
 * each philosopher always picks up the lower-numbered fork first.
 * That guarantees at least one philosopher grabs right-then-left, breaking
 * the circular-wait condition -> no deadlock.
 *
 * Alternative (noted): a Semaphore with (N-1) permits around the pick-up phase
 * caps contenders so a cycle can never close.
 */
public class DiningPhilosophers {
    private final int n;
    private final Lock[] forks;

    public DiningPhilosophers(int n) {
        this.n = n;
        this.forks = new Lock[n];
        for (int i = 0; i < n; i++) forks[i] = new ReentrantLock();
    }

    public void dine(int philosopher) {
        int left  = philosopher;
        int right = (philosopher + 1) % n;

        // Always acquire the lower-indexed fork first -> consistent global order.
        int first  = Math.min(left, right);
        int second = Math.max(left, right);

        forks[first].lock();
        try {
            forks[second].lock();
            try {
                System.out.println("Philosopher " + philosopher + " is eating");
            } finally {
                forks[second].unlock();
            }
        } finally {
            forks[first].unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int n = 5;
        DiningPhilosophers table = new DiningPhilosophers(n);
        Thread[] ts = new Thread[n];
        for (int i = 0; i < n; i++) {
            final int id = i;
            ts[i] = new Thread(() -> {
                for (int round = 0; round < 3; round++) {
                    table.dine(id);
                    try { Thread.sleep(20); } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
        System.out.println("Done — no deadlock.");
    }
}
