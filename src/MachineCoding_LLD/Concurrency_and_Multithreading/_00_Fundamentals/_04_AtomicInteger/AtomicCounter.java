package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._04_AtomicInteger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * LESSON 4 — AtomicInteger: correct counting WITHOUT a lock.
 *
 * In Lesson 2 we fixed count++ with `synchronized`, which makes threads BLOCK and wait.
 * For a single counter that's overkill. AtomicInteger gives the same correctness but is
 * LOCK-FREE and usually faster under contention.
 *
 * How? CAS = Compare-And-Swap, a single CPU instruction. incrementAndGet() effectively:
 *      do {
 *          old = read();            // e.g. 41
 *          next = old + 1;          // 42
 *      } while (!compareAndSet(old, next));  // "set to 42 ONLY IF still 41, atomically"
 * If another thread changed the value in between, compareAndSet fails and it simply
 * retries with the fresh value. No thread ever sleeps or blocks — that's "non-blocking".
 *
 * Handy methods: incrementAndGet(), getAndIncrement(), addAndGet(n),
 *                get(), set(v), compareAndSet(expected, newValue).
 *
 * WHEN IT STOPS BEING ENOUGH: atomics protect ONE variable. If you must update TWO
 * fields together as one invariant (e.g. transfer money between accounts), a single CAS
 * can't do it — go back to a lock (synchronized / ReentrantLock).
 */
public class AtomicCounter {

    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet(); // atomic ++ : read-modify-write done as one step via CAS
    }

    public int getCount() {
        return count.get();
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicCounter counter = new AtomicCounter();
        int perThread = 100_000;

        Runnable job = () -> {
            for (int i = 0; i < perThread; i++) {
                counter.increment();
            }
        };

        Thread a = new Thread(job);
        Thread b = new Thread(job);
        a.start();
        b.start();
        a.join();
        b.join();

        System.out.println("Expected: " + (2 * perThread));
        System.out.println("Actual:   " + counter.getCount() + "   <-- correct, and no lock was used.");
    }
}
