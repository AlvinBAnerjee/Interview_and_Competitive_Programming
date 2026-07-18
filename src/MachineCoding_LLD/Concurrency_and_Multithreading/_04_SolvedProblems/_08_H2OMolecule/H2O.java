package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._08_H2OMolecule;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Building H2O — group threads into molecules. (LeetCode 1117.)
 *
 * Many hydrogen and oxygen threads call {@code hydrogen()} / {@code oxygen()}.
 * They must combine into complete water molecules: every group that prints must
 * contain exactly 2 H and 1 O, and no thread of the NEXT molecule may print until
 * the current one is complete.
 *
 * Two ideas compose cleanly:
 *   1. COUNTING semaphores cap how many of each atom can enter a molecule:
 *        hSem = 2 permits  -> at most 2 hydrogens proceed
 *        oSem = 1 permit   -> at most 1 oxygen proceeds
 *   2. A CyclicBarrier(3) is the RENDEZVOUS: the 3 admitted threads wait for each
 *      other, so they bond as a group before anyone prints. The barrier is cyclic,
 *      so it automatically resets for the next molecule.
 *
 * Permits are released AFTER the barrier trips, which admits the next molecule's
 * atoms — never sooner, so molecules can't interleave.
 */
public class H2O {
    private final Semaphore hSem = new Semaphore(2); // 2 hydrogens per molecule
    private final Semaphore oSem = new Semaphore(1); // 1 oxygen  per molecule
    private final CyclicBarrier barrier = new CyclicBarrier(3);

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hSem.acquire();                 // claim one of the 2 hydrogen slots
        awaitGroup();                   // wait for the full 2H + 1O group
        releaseHydrogen.run();          // "bonding" — print H
        hSem.release();                 // free the slot for the next molecule
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oSem.acquire();                 // claim the single oxygen slot
        awaitGroup();
        releaseOxygen.run();            // print O
        oSem.release();
    }

    private void awaitGroup() {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        H2O plant = new H2O();
        // "HHHHHHOOO" -> 3 molecules; each printed group must have 2 H and 1 O.
        String input = "HHHHHHOOO";
        Thread[] ts = new Thread[input.length()];
        for (int i = 0; i < input.length(); i++) {
            char atom = input.charAt(i);
            ts[i] = new Thread(() -> {
                try {
                    if (atom == 'H') plant.hydrogen(() -> System.out.print("H"));
                    else             plant.oxygen(() -> System.out.print("O"));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        for (Thread t : ts) t.start();
        for (Thread t : ts) t.join();
        System.out.println("\nDone — every 3 chars form one molecule (2 H : 1 O).");
    }
}
