package MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.strategy.RateLimitStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._08_Medium_RateLimiter.time.TimeSource;

/**
 * The facade the application calls: {@link #allow(String)} answers "may this client make a request
 * right now?". It keeps <b>per-client</b> state in a {@link ConcurrentHashMap} and hands each client
 * its own {@link RateLimitStrategy} instance — so one client's traffic never blocks or affects
 * another's, and there is <b>no global lock</b> on the hot path.
 *
 * <h3>Concurrency</h3>
 * <ul>
 *   <li>Client state is created lazily and race-free with {@code computeIfAbsent}.</li>
 *   <li>The strategy's {@code tryAcquire} is itself lock-free (CAS), so a request touches only its
 *       own client's atomic state — the map is never globally locked.</li>
 *   <li><b>Bounded memory:</b> idle clients are evicted by {@link #evictIdle()} (call it from a
 *       scheduler, or use {@link #startBackgroundEviction}). Without this the map grows forever as
 *       new client ids appear.</li>
 * </ul>
 *
 * <p>Pattern: this is the Facade over the Strategy family; built via {@link RateLimiterFactory}.
 */
public final class RateLimiter implements AutoCloseable {

    private final ConcurrentMap<String, ClientEntry> clients = new ConcurrentHashMap<>();
    private final Supplier<RateLimitStrategy> strategyFactory;
    private final TimeSource time;
    private final long idleEvictionNanos;
    private volatile ScheduledExecutorService evictor;

    /** One client's strategy plus a last-touched stamp used only for idle eviction. */
    private static final class ClientEntry {
        final RateLimitStrategy strategy;
        volatile long lastAccessNanos;

        ClientEntry(RateLimitStrategy strategy, long now) {
            this.strategy = strategy;
            this.lastAccessNanos = now;
        }
    }

    public RateLimiter(Supplier<RateLimitStrategy> strategyFactory, TimeSource time, long idleEvictionNanos) {
        this.strategyFactory = strategyFactory;
        this.time = time;
        this.idleEvictionNanos = idleEvictionNanos;
    }

    /**
     * @return true if the request is allowed, false if the client is currently rate-limited.
     * @throws IllegalArgumentException if {@code clientId} is null.
     */
    public boolean allow(String clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("clientId must not be null");
        }
        long now = time.nanoTime();
        ClientEntry entry = clients.computeIfAbsent(clientId, k -> new ClientEntry(strategyFactory.get(), now));
        entry.lastAccessNanos = now; // for eviction; the limit decision is the strategy's alone
        return entry.strategy.tryAcquire();
    }

    /** Drop clients idle longer than the eviction threshold. @return how many were removed. */
    public int evictIdle() {
        long now = time.nanoTime();
        int[] removed = {0};
        clients.entrySet().removeIf(e -> {
            boolean stale = now - e.getValue().lastAccessNanos > idleEvictionNanos;
            if (stale) {
                removed[0]++;
            }
            return stale;
        });
        return removed[0];
    }

    public int trackedClients() {
        return clients.size();
    }

    /** Start a daemon that calls {@link #evictIdle()} every {@code periodMillis}. Idempotent. */
    public synchronized void startBackgroundEviction(long periodMillis) {
        if (evictor != null) {
            return;
        }
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "ratelimiter-evictor");
            t.setDaemon(true);
            return t;
        });
        service.scheduleAtFixedRate(this::evictIdle, periodMillis, periodMillis, TimeUnit.MILLISECONDS);
        evictor = service;
    }

    @Override
    public void close() {
        ScheduledExecutorService service = evictor;
        if (service != null) {
            service.shutdownNow();
        }
    }
}
