package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._02_ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Producer-Consumer with a hand-rolled bounded buffer using synchronized + wait/notify.
 * This is the "from scratch" answer that proves you understand monitor signalling.
 */
public class ProducerConsumerWaitNotify {

    static class BoundedBuffer {
        private final Queue<Integer> q = new LinkedList<>();
        private final int capacity;

        BoundedBuffer(int capacity) { this.capacity = capacity; }

        public synchronized void put(int item) throws InterruptedException {
            while (q.size() == capacity) {   // WHILE, not if -> re-check after wakeup
                wait();                       // releases the monitor, parks
            }
            q.add(item);
            notifyAll();                      // wake any waiting consumers
        }

        public synchronized int take() throws InterruptedException {
            while (q.isEmpty()) {
                wait();
            }
            int item = q.remove();
            notifyAll();                      // wake any waiting producers
            return item;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(5);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    buffer.put(i);
                    System.out.println("Produced " + i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    int item = buffer.take();
                    System.out.println("            Consumed " + item);
                    Thread.sleep(120);        // slower consumer -> buffer fills, producer blocks
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
