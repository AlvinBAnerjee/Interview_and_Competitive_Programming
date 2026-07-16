package MachineCoding_LLD.DesignPatterns._03_SingletonDesignPattern;

import java.util.concurrent.atomic.AtomicInteger;

// Classic thread-safe singleton via double-checked locking: the outer null-check
// skips the lock once the instance exists (cheap fast path), the inner null-check
// (taken with the lock held) stops two threads that both passed the outer check
// from both constructing an instance, and `volatile` stops another thread from
// observing a partially-constructed object due to instruction reordering.
public class DoubleCheckedLockingSingleton {

    private static volatile DoubleCheckedLockingSingleton instance;
    private static final AtomicInteger constructionCount = new AtomicInteger(0);

    private DoubleCheckedLockingSingleton() {
        constructionCount.incrementAndGet();
    }

    public static DoubleCheckedLockingSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedLockingSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return instance;
    }

    public static int getConstructionCount() {
        return constructionCount.get();
    }
}
