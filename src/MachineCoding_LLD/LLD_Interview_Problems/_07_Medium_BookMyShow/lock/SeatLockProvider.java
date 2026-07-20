package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock;

import java.time.Duration;
import java.util.List;

/**
 * The concurrency boundary of the whole system. Everything about "who may hold
 * which seat, and until when" lives behind this one interface, so the booking
 * service never touches a lock directly.
 *
 * The in-memory implementation is a single-JVM {@code ConcurrentHashMap}; a real
 * deployment swaps in a Redis/DB-backed version (e.g. {@code SET NX PX}) with the
 * exact same contract — that substitutability is the reason it's an interface.
 */
public interface SeatLockProvider {

    /**
     * Atomically hold ALL of {@code seatIds} for {@code userId} for {@code ttl}.
     * All-or-nothing: if any single seat is already held (or sold), no seat is
     * held and a {@code SeatsUnavailableException} is thrown.
     */
    void lockSeats(String showId, List<String> seatIds, String userId, Duration ttl);

    /** Release any of these seats currently held by {@code userId} (no-op otherwise). */
    void unlockSeats(String showId, List<String> seatIds, String userId);

    /** True iff {@code userId} still holds a live (non-expired) lock on EVERY seat. */
    boolean isValidLock(String showId, List<String> seatIds, String userId);

    /** True iff a live hold exists on this one seat (used to render availability). */
    boolean isHeld(String showId, String seatId);

    /** Remove and return all holds that have lapsed, so callers can notify owners. */
    List<ExpiredHold> reapExpired();
}
