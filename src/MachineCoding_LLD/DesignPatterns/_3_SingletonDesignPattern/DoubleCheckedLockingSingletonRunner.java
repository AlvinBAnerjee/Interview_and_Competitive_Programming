package MachineCoding_LLD.DesignPatterns._3_SingletonDesignPattern;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Hammers getInstance() from many threads released at (as close to) the same
// instant as possible, to prove the double-checked-locking singleton holds
// under real contention rather than a single-threaded demo that never
// exercises the race the pattern is meant to defend against.
public class DoubleCheckedLockingSingletonRunner {

    private static final int THREAD_COUNT = 50;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch startingGate = new CountDownLatch(1);

        List<Future<DoubleCheckedLockingSingleton>> futures = IntStream.range(0, THREAD_COUNT)
                .mapToObj(i -> pool.submit(() -> {
                    startingGate.await();
                    return DoubleCheckedLockingSingleton.getInstance();
                }))
                .collect(Collectors.toList());

        startingGate.countDown(); // release every thread at once
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        Set<DoubleCheckedLockingSingleton> distinctInstances = futures.stream()
                .map(DoubleCheckedLockingSingletonRunner::getUnchecked)
                .collect(Collectors.toSet());

        System.out.println(THREAD_COUNT + " threads raced to call getInstance()");
        System.out.println("Distinct instances observed: " + distinctInstances.size());
        System.out.println("Constructor actually ran: "
                + DoubleCheckedLockingSingleton.getConstructionCount() + " time(s)");
        System.out.println("Same instance everywhere: " + (distinctInstances.size() == 1));
    }

    private static DoubleCheckedLockingSingleton getUnchecked(Future<DoubleCheckedLockingSingleton> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
