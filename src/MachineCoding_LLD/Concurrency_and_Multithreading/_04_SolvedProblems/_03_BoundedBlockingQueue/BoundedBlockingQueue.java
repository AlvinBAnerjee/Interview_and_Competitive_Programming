package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._03_BoundedBlockingQueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A bounded blocking queue built from scratch with ReentrantLock + two Conditions.
 * Demonstrates why Condition beats a single wait/notify monitor: we can signal
 * exactly the waiter group we unblocked (producers vs consumers).
 */
public class BoundedBlockingQueue<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull  = lock.newCondition(); // producers await here
    private final Condition notEmpty = lock.newCondition(); // consumers await here

    public BoundedBlockingQueue(int capacity) { this.capacity = capacity; }

    public void enqueue(T element) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();          // release lock, park until a consumer signals
            }
            queue.add(element);
            notEmpty.signal();            // wake ONE waiting consumer
        } finally {
            lock.unlock();
        }
    }

    public T dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            T element = queue.remove();
            notFull.signal();             // wake ONE waiting producer
            return element;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    // ---- demo ----
    public static void main(String[] args) throws InterruptedException {
        BoundedBlockingQueue<Integer> bq = new BoundedBlockingQueue<>(3);

        Runnable producer = () -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    bq.enqueue(i);
                    System.out.println("enqueued " + i + " (size=" + bq.size() + ")");
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };

        Runnable consumer = () -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    Thread.sleep(80);     // slow consumer -> producer blocks at capacity
                    System.out.println("        dequeued " + bq.dequeue());
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };

        Thread p = new Thread(producer), c = new Thread(consumer);
        p.start(); c.start();
        p.join();  c.join();
    }
}
