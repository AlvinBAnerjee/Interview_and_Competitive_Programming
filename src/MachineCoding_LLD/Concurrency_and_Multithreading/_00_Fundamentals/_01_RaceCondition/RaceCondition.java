package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._01_RaceCondition;

/**
 * LESSON 1 — The Race Condition (the WHY behind everything else).
 *
 * Two threads each add 1 to a shared counter 100,000 times.
 * Expected total: 200,000. But run it a few times — you'll usually get LESS.
 *
 * Why? Because `count++` is NOT one step. It is really three:
 *      1. READ  count from memory      (say count == 41)
 *      2. ADD   1 to the read value    (42 in a CPU register)
 *      3. WRITE 42 back to memory
 *
 * If thread A and thread B both READ 41 before either WRITES, they both write 42.
 * Two increments happened, but the counter only went up by ONE. This "lost update"
 * is a RACE CONDITION: the result depends on the exact timing of the threads.
 *
 * The block of code that touches shared data (here, count++) is the CRITICAL SECTION.
 * The whole rest of this Fundamentals folder is about protecting it.
 */
public class RaceCondition {

    // Plain field, no protection at all. This is the bug.
    private int count = 0;

    public void increment() {
        count++; // read-modify-write: three steps masquerading as one
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        RaceCondition counter = new RaceCondition();
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
        a.join();  // wait for both to finish before we read the result
        b.join();

        System.out.println("Expected: " + (2 * perThread));
        System.out.println("Actual:   " + counter.getCount() + "   <-- usually smaller (lost updates!)");
        System.out.println("Run it a few times: the number changes. That is the race.");
    }
}
