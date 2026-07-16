package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._01_PrintInOrder;

import java.util.concurrent.Semaphore;

/**
 * Print "foobar" n times, alternating across two threads. (LeetCode 1115.)
 *
 * Semaphore handshake:
 *   - fooTurn starts with 1 permit  -> foo goes first
 *   - barTurn starts with 0 permits -> bar must wait
 * Each thread acquires its own permit, prints, then releases the OTHER thread's
 * permit. The permits ARE the turn signal, so no explicit lock is needed.
 */
public class FooBar {
    private final int n;
    private final Semaphore fooTurn = new Semaphore(1); // foo may run first
    private final Semaphore barTurn = new Semaphore(0); // bar blocked initially

    public FooBar(int n) { this.n = n; }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            fooTurn.acquire();      // wait for my turn
            printFoo.run();
            barTurn.release();      // hand the turn to bar
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            barTurn.acquire();
            printBar.run();
            fooTurn.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FooBar fb = new FooBar(5);
        Thread a = new Thread(() -> {
            try { fb.foo(() -> System.out.print("foo")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread b = new Thread(() -> {
            try { fb.bar(() -> System.out.print("bar")); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        a.start();
        b.start();
        a.join();
        b.join();
        System.out.println("\nExpected: foobar x5");
    }
}
