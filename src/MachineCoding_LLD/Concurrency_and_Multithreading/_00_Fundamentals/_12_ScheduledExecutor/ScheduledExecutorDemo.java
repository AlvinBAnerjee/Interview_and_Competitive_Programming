package MachineCoding_LLD.Concurrency_and_Multithreading._00_Fundamentals._12_ScheduledExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * LESSON 12 — ScheduledExecutorService: run tasks LATER, or REPEATEDLY.
 *
 * A plain pool runs a task as soon as a worker is free. A ScheduledExecutorService adds TIME:
 * "run this in 2 seconds", or "run this every 500 ms". It replaces the old java.util.Timer
 * (which used a single thread and let one bad task kill all future runs).
 *
 * THE THREE SCHEDULING METHODS:
 *   schedule(task, delay, unit)
 *       -> run ONCE after `delay`.
 *
 *   scheduleAtFixedRate(task, initialDelay, period, unit)
 *       -> run repeatedly. Each run starts `period` after the PREVIOUS run STARTED.
 *          The clock ticks on a fixed cadence regardless of how long the task takes.
 *          ⚠️ If a run takes LONGER than `period`, the next run starts immediately after,
 *          and runs can "pile up". Good for: sampling a value on a steady beat.
 *
 *   scheduleWithFixedDelay(task, initialDelay, delay, unit)
 *       -> run repeatedly. Each run starts `delay` after the PREVIOUS run FINISHED.
 *          The gap between runs is constant; total cadence drifts with task duration.
 *          Good for: polling where you want breathing room between calls.
 *
 * MEMORY HOOK:  fixed-RATE measures start->start;  fixed-DELAY measures end->start.
 */
public class ScheduledExecutorDemo {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // 1) One-shot: fire once after 1 second.
        scheduler.schedule(
                () -> System.out.println("[schedule] one-shot fired after ~1s"),
                1, TimeUnit.SECONDS);

        // 2) Periodic at a FIXED RATE: start-to-start every 500ms, beginning immediately.
        AtomicInteger ticks = new AtomicInteger();
        ScheduledFuture<?> heartbeat = scheduler.scheduleAtFixedRate(
                () -> System.out.println("[fixedRate] tick #" + ticks.incrementAndGet()),
                0, 500, TimeUnit.MILLISECONDS);

        // Let the heartbeat run for ~2.2s (=> about 5 ticks), then cancel just that task.
        Thread.sleep(2200);
        heartbeat.cancel(false);                 // stop the repeating task; false = let a run in progress finish
        System.out.println("[fixedRate] cancelled after " + ticks.get() + " ticks");

        // A periodic task NEVER lets the pool terminate on its own — you must shut it down.
        scheduler.shutdown();
        scheduler.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("scheduler shut down cleanly.");
    }
}
