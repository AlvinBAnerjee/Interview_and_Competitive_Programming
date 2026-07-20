package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock;

import java.time.Instant;
import java.util.List;

/**
 * The receipt {@code holdSeats} hands back — proof the caller holds a hold on
 * exactly these seats until {@code expiresAt}. Passed to {@code confirmBooking},
 * which re-validates it against the live locks (a token alone is not trusted).
 */
public record LockToken(String showId, List<String> seatIds, String userId, Instant expiresAt) {
    public LockToken {
        seatIds = List.copyOf(seatIds);
    }
    public boolean isExpired() { return Instant.now().isAfter(expiresAt); }
}
