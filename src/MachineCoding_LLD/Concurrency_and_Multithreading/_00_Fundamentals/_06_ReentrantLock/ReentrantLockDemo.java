package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._06_ReentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LESSON 6 — ReentrantLock: everything synchronized gives you, plus what it can't.
 *
 * Part 1 is the exact same correctness demo as Lesson 2's SynchronizedCounter, but with
 * an explicit Lock instead of the `synchronized` keyword. lock()/unlock() must be paired
 * manually — unlike `synchronized`, the JVM will NOT release the lock for you, so unlock()
 * belongs in a `finally` block.
 *
 * Part 2 demonstrates the actual reason to reach for ReentrantLock over synchronized:
 * tryLock() lets a thread ASK for the lock without blocking forever.
 */
public class ReentrantLockDemo {

    private final Lock lock = new ReentrantLock();
    private int count = 0;

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock(); // MUST run even if the critical section throws
        }
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        // --- Part 1: correctness, same shape as the synchronized counter ---
        ReentrantLockDemo counter = new ReentrantLockDemo();
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
        System.out.println("Actual:   " + counter.getCount());

        // --- Part 2: tryLock() — synchronized simply cannot do this ---
        Lock resource = new ReentrantLock();

        Thread holder = new Thread(() -> {
            resource.lock();
            try {
                System.out.println("holder: got the lock, sleeping 300ms...");
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                resource.unlock();
                System.out.println("holder: released the lock.");
            }
        });
        holder.start();
        Thread.sleep(50); // let holder grab the lock first

        System.out.println("main: tryLock() immediately         -> " + resource.tryLock());
        // false above: holder still has it, and we did NOT block waiting.

        System.out.println("main: tryLock(500ms, TimeUnit.MILLISECONDS) -> "
                + resource.tryLock(500, TimeUnit.MILLISECONDS));
        // true above: we waited up to 500ms, holder released within 300ms, so we got it.

        resource.unlock(); // release the lock we just acquired via the second tryLock()
        holder.join();
    }
}
