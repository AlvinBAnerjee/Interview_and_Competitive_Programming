package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._07_FizzBuzzMultithreaded;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * Multithreaded FizzBuzz. (LeetCode 1195.)
 *
 * Four threads share one counter and print 1..n:
 *   - number  : i not divisible by 3 or 5   -> "i"
 *   - fizz    : i divisible by 3 not 5       -> "fizz"
 *   - buzz    : i divisible by 5 not 3       -> "buzz"
 *   - fizzbuzz: i divisible by 15            -> "fizzbuzz"
 *
 * Design: one lock + one condition + a shared {@code current} counter. Each
 * method has the exact same shape:
 *   1. Grab the lock.
 *   2. Sleep (condition.await()) while it's not my turn and we're not done.
 *   3. If we're done, wake everyone else up and return.
 *   4. Otherwise print, advance the counter, and signalAll() so whichever
 *      thread owns the next value can wake up and take its turn.
 * The four methods are written out in full (rather than sharing a helper)
 * because that's the version that's easiest to read and explain line by line.
 */
public class FizzBuzz {
    private final int n;
    private int current = 1;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public FizzBuzz(int n) {
        this.n = n;
    }

    public void fizz(Runnable printFizz) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (current <= n && !(current % 3 == 0 && current % 5 != 0)) {
                    condition.await();
                }
                if (current > n) {
                    condition.signalAll();
                    return;
                }
                printFizz.run();
                current++;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (current <= n && !(current % 5 == 0 && current % 3 != 0)) {
                    condition.await();
                }
                if (current > n) {
                    condition.signalAll();
                    return;
                }
                printBuzz.run();
                current++;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (current <= n && current % 15 != 0) {
                    condition.await();
                }
                if (current > n) {
                    condition.signalAll();
                    return;
                }
                printFizzBuzz.run();
                current++;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public void number(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                while (current <= n && (current % 3 == 0 || current % 5 == 0)) {
                    condition.await();
                }
                if (current > n) {
                    condition.signalAll();
                    return;
                }
                printNumber.accept(current);
                current++;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FizzBuzz fb = new FizzBuzz(15);
        Thread[] ts = {
            new Thread(wrap(() -> fb.fizz(() -> System.out.print("fizz ")))),
            new Thread(wrap(() -> fb.buzz(() -> System.out.print("buzz ")))),
            new Thread(wrap(() -> fb.fizzbuzz(() -> System.out.print("fizzbuzz ")))),
            new Thread(wrap(() -> fb.number(i -> System.out.print(i + " ")))),
        };
        for (Thread t : ts) t.start();
        for (Thread t : ts) t.join();
        System.out.println("\nExpected: 1 2 fizz 4 buzz fizz 7 8 fizz buzz 11 fizz 13 14 fizzbuzz");
    }

    private interface Task { void run() throws InterruptedException; }

    private static Runnable wrap(Task t) {
        return () -> {
            try { t.run(); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };
    }
}
