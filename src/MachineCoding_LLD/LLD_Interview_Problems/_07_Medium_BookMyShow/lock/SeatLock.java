package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock;

import java.time.Instant;

/**
 * A time-boxed hold on one seat: who owns it and when it lapses. Immutable — a
 * new hold is a new value, which is what lets the lock provider use
 * ConcurrentHashMap's atomic {@code compute}/{@code remove(k,v)} on it.
 */
public record SeatLock(String userId, Instant expiresAt) {
    public boolean isExpired() { return Instant.now().isAfter(expiresAt); }
}
