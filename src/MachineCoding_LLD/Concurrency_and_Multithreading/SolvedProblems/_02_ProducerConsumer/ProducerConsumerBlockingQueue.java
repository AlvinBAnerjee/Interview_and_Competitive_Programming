package MachineCoding_LLD.Concurrency_and_Multithreading.SolvedProblems._02_ProducerConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The PRODUCTION answer: a BlockingQueue handles all the blocking for you.
 * put() blocks when full, take() blocks when empty. No locks, no wait/notify.
 */
public class ProducerConsumerBlockingQueue {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        final int POISON = -1;   // sentinel to signal shutdown

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    queue.put(i);                 // blocks if full
                    System.out.println("Produced " + i);
                }
                queue.put(POISON);                // tell consumer to stop
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    int item = queue.take();      // blocks if empty
                    if (item == POISON) break;
                    System.out.println("            Consumed " + item);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
