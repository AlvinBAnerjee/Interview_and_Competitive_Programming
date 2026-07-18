package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._07_FizzBuzzMultithreaded;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

/**
 * Multithreaded FizzBuzz. (LeetCode 1195.)
 *
 * Four threads share one counter and print 1..n:
 *   - number  : i not divisible by 3 or 5   -> "i"
 *   - fizz    : i divisible by 3 not 5       -> "fizz"
 *   - buzz    : i divisible by 5 not 3       -> "buzz"
 *   - fizzbuzz: i divisible by 15            -> "fizzbuzz"
 *
 * This is the N-way generalization of the two-thread "print in order" ping-pong
 * ({@code FooBar}). Instead of a fixed alternation, WHICH thread runs next depends
 * on the current value, so a plain semaphore hand-off doesn't fit — the turn is
 * decided by a PREDICATE on the shared counter.
 *
 * Design: one lock + one condition + a shared {@code current} counter. Each thread
 * waits (blocking, never busy-waiting) until {@code current} is a value it owns,
 * prints, advances the counter, and {@code signalAll()}s so the owner of the next
 * value can wake. Termination: once {@code current > n}, every waiter wakes and
 * returns, so no thread is left blocked.
 */
public class FizzBuzz {
    private final int n;
    private int current = 1;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();

    public FizzBuzz(int n) { this.n = n; }

    public void fizz(Runnable printFizz) throws InterruptedException {
        run(i -> i % 3 == 0 && i % 5 != 0, i -> printFizz.run());
    }

    public void buzz(Runnable printBuzz) throws InterruptedException {
        run(i -> i % 5 == 0 && i % 3 != 0, i -> printBuzz.run());
    }

    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        run(i -> i % 15 == 0, i -> printFizzBuzz.run());
    }

    public void number(IntConsumer printNumber) throws InterruptedException {
        run(i -> i % 3 != 0 && i % 5 != 0, printNumber);
    }

    /** Block until {@code current} is a value this thread owns, print it, advance, signal. */
    private void run(IntPredicate mine, IntConsumer print) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                // wait while it's neither my turn nor the end
                while (current <= n && !mine.test(current)) cond.await();
                if (current > n) return;          // all done — release the thread
                print.accept(current);
                current++;
                cond.signalAll();                 // wake whoever owns current+1 (and finishers)
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
