package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock;

import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.SeatsUnavailableException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory seat locks over a single {@link ConcurrentHashMap} keyed by
 * "showId::seatId". This is where double-booking is actually prevented.
 *
 * The one primitive that does the work is {@link ConcurrentHashMap#compute}:
 * the map holds a per-key (per-seat) lock for the duration of the remapping
 * lambda, so "is this seat free? if so, claim it" is a single ATOMIC step — no
 * check-then-act (TOCTOU) window for two threads to both slip through. Under
 * contention on one seat, exactly one thread's lambda installs its lock; every
 * other sees a live lock and loses.
 *
 * Expiry is handled two ways, and the first is what guarantees correctness:
 *   1. LAZY — {@code compute} treats an expired lock as free and overwrites it,
 *      so a stale hold can never block a new booking even if the reaper never runs.
 *   2. EAGER — {@link #reapExpired()} (driven by a scheduler) removes lapsed
 *      holds so memory is reclaimed and owners can be notified.
 */
public class InMemorySeatLockProvider implements SeatLockProvider {

    private final ConcurrentHashMap<String, SeatLock> locks = new ConcurrentHashMap<>();

    private static String key(String showId, String seatId) {
        return showId + "::" + seatId;
    }

    @Override
    public void lockSeats(String showId, List<String> seatIds, String userId, Duration ttl) {
        // Acquire in a deterministic (sorted) order. With CAS-style compute there is no
        // blocking, so this isn't needed to avoid deadlock — but it keeps two overlapping
        // multi-seat requests from livelocking and makes rollback order predictable.
        List<String> ordered = new ArrayList<>(seatIds);
        Collections.sort(ordered);

        SeatLock desired = new SeatLock(userId, Instant.now().plus(ttl));
        List<String> acquired = new ArrayList<>(ordered.size());

        for (String seatId : ordered) {
            String k = key(showId, seatId);
            // Claim iff currently free or the previous hold has lapsed. Atomic per key.
            SeatLock result = locks.compute(k, (kk, current) ->
                    (current == null || current.isExpired()) ? desired : current);

            if (result == desired) {                 // identity check: our lock is the one installed
                acquired.add(k);
            } else {
                // Someone else holds this seat -> all-or-nothing: undo our earlier claims.
                for (String owned : acquired) {
                    locks.remove(owned, desired);     // conditional: only remove if still ours
                }
                throw new SeatsUnavailableException(showId, seatId);
            }
        }
    }

    @Override
    public void unlockSeats(String showId, List<String> seatIds, String userId) {
        for (String seatId : seatIds) {
            String k = key(showId, seatId);
            SeatLock current = locks.get(k);
            if (current != null && current.userId().equals(userId)) {
                locks.remove(k, current);             // conditional: don't clobber a newer holder
            }
        }
    }

    @Override
    public boolean isValidLock(String showId, List<String> seatIds, String userId) {
        for (String seatId : seatIds) {
            SeatLock current = locks.get(key(showId, seatId));
            if (current == null || current.isExpired() || !current.userId().equals(userId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isHeld(String showId, String seatId) {
        SeatLock current = locks.get(key(showId, seatId));
        return current != null && !current.isExpired();
    }

    @Override
    public List<ExpiredHold> reapExpired() {
        List<ExpiredHold> expired = new ArrayList<>();
        for (Map.Entry<String, SeatLock> e : locks.entrySet()) {
            SeatLock lock = e.getValue();
            if (lock.isExpired() && locks.remove(e.getKey(), lock)) {   // only if not already replaced
                String[] parts = e.getKey().split("::", 2);
                expired.add(new ExpiredHold(parts[0], parts[1], lock.userId()));
            }
        }
        return expired;
    }
}
