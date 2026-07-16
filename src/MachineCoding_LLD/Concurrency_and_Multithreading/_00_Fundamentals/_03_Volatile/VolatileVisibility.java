package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._03_Volatile;

/**
 * LESSON 3 — `volatile` fixes VISIBILITY (a different bug from Lesson 1).
 *
 * The problem this time is NOT atomicity — it's that one thread's write may never be
 * SEEN by another. For speed, each CPU core can cache a copy of a variable. A worker
 * thread might keep reading its own stale cached copy of `running` forever, never
 * noticing that main() set it to false. Result: the worker never stops (a hang).
 *
 * `volatile` tells the JVM: "always read/write this field straight from main memory,
 * never a cached copy, and don't reorder around it." A volatile write is guaranteed
 * to be visible to the next volatile read (a happens-before relationship).
 *
 * TRY THE EXPERIMENT: remove the `volatile` keyword below and re-run. On many JVMs the
 * program will HANG forever, because the worker never sees running == false.
 *
 * ⚠️ HUGE CAVEAT: volatile gives VISIBILITY, NOT atomicity. `volatileCount++` is STILL
 * broken (it's still read-modify-write). Use volatile for flags/signals, not counters.
 * For a counter that many threads mutate, you need Lesson 2 (synchronized) or Lesson 4
 * (AtomicInteger).
 */
public class VolatileVisibility {

    // Remove `volatile` to (likely) see the program hang forever.
    private volatile boolean running = true;

    public static void main(String[] args) throws InterruptedException {
        VolatileVisibility demo = new VolatileVisibility();

        Thread worker = new Thread(() -> {
            long spins = 0;
            while (demo.running) {   // reads `running` on every loop
                spins++;
            }
            System.out.println("Worker saw running == false and stopped after " + spins + " spins.");
        });

        worker.start();
        Thread.sleep(50);            // let the worker get going and cache `running`

        System.out.println("main(): setting running = false");
        demo.running = false;        // with volatile, the worker sees this promptly

        worker.join(2000);           // wait up to 2s for it to notice
        if (worker.isAlive()) {
            System.out.println("Worker is STILL running — visibility bug! (Did you remove `volatile`?)");
            System.exit(0);          // force-quit the hung demo
        }
        System.out.println("Done.");
    }
}
