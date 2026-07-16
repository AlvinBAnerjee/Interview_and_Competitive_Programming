package MachineCoding_LLD.Concurrency_and_Multithreading.SolvedProblems._06_RateLimiter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe token-bucket rate limiter.
 *
 * Refill is time-based: tokens accrue at `refillPerSec`, capped at `capacity`.
 * The refill-then-spend step is guarded by synchronized so the shared
 * (tokens, lastRefill) pair updates atomically. Simple and correct; a lock-free
 * CAS version packs both fields into one AtomicLong (mentioned in PROBLEM.md).
 */
public class TokenBucketRateLimiter {
    private final int capacity;
    private final double refillPerSec;
    private double tokens;
    private long lastRefillNanos;

    public TokenBucketRateLimiter(int capacity, double refillPerSec) {
        this.capacity = capacity;
        this.refillPerSec = refillPerSec;
        this.tokens = capacity;              // start full -> allow an initial burst
        this.lastRefillNanos = System.nanoTime();
    }

    public synchronized boolean tryAcquire() {
        refill();
        if (tokens >= 1.0) {
            tokens -= 1.0;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        double elapsedSec = (now - lastRefillNanos) / 1_000_000_000.0;
        double refilled = elapsedSec * refillPerSec;
        if (refilled > 0) {
            tokens = Math.min(capacity, tokens + refilled);
            lastRefillNanos = now;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 5 tokens capacity, refilling 5/sec. Fire 20 concurrent requests immediately:
        // ~5 should pass (initial burst), the rest rejected until tokens refill.
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 5);
        AtomicInteger allowed = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();

        int requests = 20;
        Thread[] ts = new Thread[requests];
        for (int i = 0; i < requests; i++) {
            ts[i] = new Thread(() -> {
                if (limiter.tryAcquire()) allowed.incrementAndGet();
                else rejected.incrementAndGet();
            });
            ts[i].start();
        }
        for (Thread t : ts) t.join();

        System.out.println("Allowed:  " + allowed.get() + " (expected ~5 burst)");
        System.out.println("Rejected: " + rejected.get());
    }
}
