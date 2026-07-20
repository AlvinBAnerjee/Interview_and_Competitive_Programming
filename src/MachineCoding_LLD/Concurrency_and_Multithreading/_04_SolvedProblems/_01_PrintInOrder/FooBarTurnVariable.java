package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._01_PrintInOrder;

/**
 * Print "foobar" n times, alternating across two threads — using a shared
 * `turn` variable + synchronized wait/notify instead of Semaphores.
 *
 * turn == true  -> foo's turn
 * turn == false -> bar's turn
 * Each thread waits (in a loop, guarding against spurious wakeups) until the
 * turn variable matches its identity, prints, flips it, then notifies the
 * other thread.
 */
public class  FooBarTurnVariable {
    private final int n;
    private final Object lock = new Object();
    private boolean turn = true; // true -> foo goes first

    public FooBarTurnVariable(int n) { this.n = n; }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (!turn) lock.wait();   // wait until it's foo's turn
                printFoo.run();
                turn = false;                 // hand the turn to bar
                lock.notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (turn) lock.wait();    // wait until it's bar's turn
                printBar.run();
                turn = true;                   // hand the turn to foo
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FooBarTurnVariable fb = new FooBarTurnVariable(5);
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
