package MachineCoding_LLD.Concurrency_and_Multithreading.SolvedProblems._04_ThreadSafeSingleton;

/**
 * Four correct thread-safe singletons, from "works but locks a lot" to "best practice".
 */
public class Singletons {

    /** 1) Double-checked locking. The `volatile` is MANDATORY (see PROBLEM.md). */
    static class DclSingleton {
        private static volatile DclSingleton instance;   // volatile => no reordering / safe publish
        private DclSingleton() { }

        public static DclSingleton getInstance() {
            if (instance == null) {                       // 1st check (no lock, fast path)
                synchronized (DclSingleton.class) {
                    if (instance == null) {               // 2nd check (under lock)
                        instance = new DclSingleton();
                    }
                }
            }
            return instance;
        }
    }

    /** 2) Bill Pugh holder idiom — lazy AND lock-free. Preferred for most cases.
     *  The nested class isn't loaded until getInstance() is first called; class
     *  initialization is guaranteed thread-safe by the JVM. */
    static class HolderSingleton {
        private HolderSingleton() { }
        private static class Holder {
            private static final HolderSingleton INSTANCE = new HolderSingleton();
        }
        public static HolderSingleton getInstance() {
            return Holder.INSTANCE;
        }
    }

    /** 3) Enum singleton — serialization- and reflection-safe (Effective Java's pick). */
    enum EnumSingleton {
        INSTANCE;
        public void doWork() { System.out.println("working via enum singleton"); }
    }

    public static void main(String[] args) throws InterruptedException {
        // Hammer getInstance() from many threads; every reference must be identical.
        int threads = 50;
        DclSingleton[] seen = new DclSingleton[threads];
        Thread[] ts = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            final int idx = i;
            ts[i] = new Thread(() -> seen[idx] = DclSingleton.getInstance());
            ts[i].start();
        }
        for (Thread t : ts) t.join();

        boolean allSame = true;
        for (DclSingleton s : seen) allSame &= (s == seen[0]);
        System.out.println("DCL: all threads saw the same instance? " + allSame);
        System.out.println("Holder same instance? "
                + (HolderSingleton.getInstance() == HolderSingleton.getInstance()));
        EnumSingleton.INSTANCE.doWork();
    }
}
