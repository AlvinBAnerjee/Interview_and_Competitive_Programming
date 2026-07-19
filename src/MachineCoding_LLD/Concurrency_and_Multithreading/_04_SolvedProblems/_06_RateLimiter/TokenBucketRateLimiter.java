package MachineCoding_LLD.Concurrency_and_Multithreading._04_SolvedProblems._06_RateLimiter;

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
        // 5 tokens capacity, refilling 5/sec.
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 5);

        // Round 1: fire 20 concurrent requests immediately -> ~5 pass (initial
        // burst, bucket starts full), the rest are rejected and drain it to 0.
        int allowed1 = fireBurst(limiter, 20);
        System.out.println("Round 1 allowed: " + allowed1 + " (expected ~5 burst)");

        // Sleeping here lets tokens refill (5/sec), independent of round 1's
        // requests -- a uniform sleep *inside* the racing threads wouldn't
        // do this, since every thread would still hit tryAcquire() at the
        // same instant and get capped at `capacity` regardless of the delay.
        Thread.sleep(2000);

        // Round 2: bucket has refilled (capped at capacity=5), so another
        // ~5 should be allowed -- proving refill actually happened.
        int allowed2 = fireBurst(limiter, 20);
        System.out.println("Round 2 allowed: " + allowed2 + " (expected ~5 burst, refill worked)");
    }

    private static int fireBurst(TokenBucketRateLimiter limiter, int requests) throws InterruptedException {
        AtomicInteger allowed = new AtomicInteger();
        Thread[] ts = new Thread[requests];
        for (int i = 0; i < requests; i++) {
            ts[i] = new Thread(() -> {
                if (limiter.tryAcquire()) allowed.incrementAndGet();
            });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
        return allowed.get();
    }
}
