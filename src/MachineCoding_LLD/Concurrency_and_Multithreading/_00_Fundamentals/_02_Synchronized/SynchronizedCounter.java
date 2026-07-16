package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._02_Synchronized;

/**
 * LESSON 2 — `synchronized` fixes the race from Lesson 1.
 *
 * `synchronized` gives you MUTUAL EXCLUSION: only ONE thread at a time can hold an
 * object's intrinsic lock (its "monitor"). Every other thread that reaches a
 * synchronized block on the same object must WAIT until the lock is released.
 *
 * So the three steps of count++ (read, add, write) now happen as one uninterruptible
 * unit — no other thread can sneak in between them. Result is ALWAYS 200,000.
 *
 * BONUS: entering/leaving a synchronized block also flushes changes to main memory,
 * so it fixes VISIBILITY too (not just atomicity). synchronized = atomicity + visibility.
 *
 * Two equivalent styles are shown. Golden rule: keep the critical section SMALL —
 * lock only the shared-state access, not slow work like I/O.
 */
public class SynchronizedCounter {

    private int count = 0;
    private final Object lock = new Object(); // a dedicated lock object (good practice)

    // Style A: synchronized METHOD — locks `this` for the whole method body.
    public synchronized void incrementMethod() {
        count++;
    }

    // Style B: synchronized BLOCK — locks only the lines you wrap. More precise.
    public void incrementBlock() {
        // ...do non-shared work out here, unlocked...
        synchronized (lock) {
            count++;               // <-- only the critical section is inside the lock
        }
    }

    public synchronized int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedCounter counter = new SynchronizedCounter();
        int perThread = 100_000;

        Runnable job = () -> {
            for (int i = 0; i < perThread; i++) {
                counter.incrementMethod(); // try swapping to incrementBlock() — same result
            }
        };

        Thread a = new Thread(job);
        Thread b = new Thread(job);
        a.start();
        b.start();
        a.join();
        b.join();

        System.out.println("Expected: " + (2 * perThread));
        System.out.println("Actual:   " + counter.getCount() + "   <-- ALWAYS correct now, every run.");
    }
}
