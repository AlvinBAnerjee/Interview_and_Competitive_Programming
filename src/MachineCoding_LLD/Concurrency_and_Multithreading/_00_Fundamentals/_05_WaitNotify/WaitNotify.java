package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._05_WaitNotify;

/**
 * LESSON 5 — wait() / notify(): threads COORDINATING, not just counting.
 *
 * So far threads competed for data. Sometimes a thread must instead WAIT for a
 * condition another thread will create. Example here: a one-slot mailbox.
 *   - Consumer wants to take() a message, but there's nothing yet -> it must wait.
 *   - Producer put()s a message -> it wakes the waiting consumer.
 *
 * The tools (all called ONLY while holding the object's lock, i.e. inside synchronized):
 *   - wait()      -> RELEASE the lock and go to sleep until notified. (Busy-waiting in a
 *                    loop with sleep() would waste CPU; wait() sleeps efficiently.)
 *   - notify()    -> wake ONE waiting thread.
 *   - notifyAll() -> wake ALL waiting threads (usually the safer default).
 *
 * TWO RULES YOU MUST NEVER BREAK:
 *   1. Only call wait/notify while SYNCHRONIZED on that same object. Otherwise:
 *      IllegalMonitorStateException.
 *   2. ALWAYS wait INSIDE A while-LOOP that re-checks the condition — never an `if`.
 *      A thread can wake up "spuriously" (without a real notify), or another thread may
 *      have grabbed the item first. The loop re-checks and goes back to sleep if needed.
 *
 * (In real code you'd usually just use a BlockingQueue, which hides all of this. This
 *  lesson shows what's happening underneath.)
 */
public class WaitNotify {

    private String message = null;                 // the single slot; null == empty
    private final Object lock = new Object();

    public void put(String msg) throws InterruptedException {
        synchronized (lock) {
            while (message != null) {              // slot full? wait for the consumer to drain it
                lock.wait();                       // releases lock, sleeps, re-acquires on wakeup
            }
            message = msg;
            System.out.println("Producer put:  " + msg);
            lock.notifyAll();                      // wake the consumer waiting on an empty slot
        }
    }

    public String take() throws InterruptedException {
        synchronized (lock) {
            while (message == null) {              // slot empty? wait for the producer
                lock.wait();
            }
            String msg = message;
            message = null;                        // drain the slot
            System.out.println("Consumer took: " + msg);
            lock.notifyAll();                      // wake the producer waiting on a full slot
            return msg;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaitNotify mailbox = new WaitNotify();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    mailbox.put("msg-" + i);
                    Thread.sleep((long) (100*Math.random()));             // produce a little slower than instant
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    mailbox.take();
                    Thread.sleep((long) (100*Math.random()));             // produce a little slower than instant
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        // Start the consumer FIRST so it has to wait() for the first message —
        // proving the handshake works even when the taker arrives early.
        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        System.out.println("All 5 messages handed off in order, no lost or duplicated items.");
    }
}
