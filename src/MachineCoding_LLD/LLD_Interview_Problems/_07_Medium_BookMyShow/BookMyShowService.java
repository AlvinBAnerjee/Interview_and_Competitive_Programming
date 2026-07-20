package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow;

import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.ExpiredHold;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.LockToken;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.SeatLockProvider;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Booking;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.BookingStatus;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Seat;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.SeatMap;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.SeatStatus;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Show;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.ShowSeat;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.observer.BookingObserver;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment.PaymentResult;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment.PaymentStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.pricing.PricingStrategy;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Facade over the whole booking flow: catalog search, availability, hold, and
 * confirm. Coordinates the {@link SeatLockProvider} (concurrency), the pricing
 * and payment {@link PricingStrategy}/{@link PaymentStrategy} strategies, and
 * {@link BookingObserver}s — but delegates the hard guarantee (no double-book)
 * entirely to the lock provider.
 *
 * The two-phase booking (hold, then confirm) is deliberate: it bounds how long a
 * seat can sit reserved while a user fills in payment, and guarantees the seat is
 * freed automatically if they abandon checkout.
 */
public class BookMyShowService {

    private final SeatLockProvider lockProvider;
    private final PricingStrategy pricingStrategy;
    private final Duration holdTtl;

    private final Map<String, Show> showsById = new LinkedHashMap<>();
    private final List<BookingObserver> observers = new CopyOnWriteArrayList<>();
    private final AtomicLong bookingSeq = new AtomicLong(1000);
    private final ScheduledExecutorService reaper =
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "hold-reaper");
                t.setDaemon(true);
                return t;
            });

    public BookMyShowService(SeatLockProvider lockProvider, PricingStrategy pricingStrategy, Duration holdTtl) {
        this.lockProvider = lockProvider;
        this.pricingStrategy = pricingStrategy;
        this.holdTtl = holdTtl;
    }

    // ---- catalog wiring ----

    public void addShow(Show show) { showsById.put(show.id(), show); }

    public void addObserver(BookingObserver observer) { observers.add(observer); }

    /** Sweep lapsed holds every {@code period} and notify observers. */
    public void startExpiryReaper(Duration period) {
        long ms = Math.max(1, period.toMillis());
        reaper.scheduleAtFixedRate(this::reapExpiredHolds, ms, ms, TimeUnit.MILLISECONDS);
    }

    public void shutdown() { reaper.shutdownNow(); }

    // ---- read side ----

    /** Shows of a given movie playing in a given city. */
    public List<Show> searchShows(String movieId, String cityId) {
        List<Show> matches = new ArrayList<>();
        for (Show show : showsById.values()) {
            if (show.movie().id().equals(movieId) && show.theatre().city().id().equals(cityId)) {
                matches.add(show);
            }
        }
        return matches;
    }

    /** Snapshot of every seat's status for a show (booked / held / available). */
    public SeatMap getAvailability(String showId) {
        Show show = requireShow(showId);
        Map<String, SeatStatus> statuses = new LinkedHashMap<>();
        List<Seat> seats = new ArrayList<>();
        for (ShowSeat ss : show.showSeats()) {
            Seat seat = ss.seat();
            seats.add(seat);
            SeatStatus status = ss.isBooked() ? SeatStatus.BOOKED
                    : lockProvider.isHeld(showId, seat.id()) ? SeatStatus.HELD
                    : SeatStatus.AVAILABLE;
            statuses.put(seat.id(), status);
        }
        return new SeatMap(showId, seats, statuses);
    }

    // ---- write side: the two-phase booking ----

    /**
     * Phase 1 — temporarily hold {@code seatIds} for {@code userId}. All-or-nothing.
     * Throws {@link SeatsUnavailableException} if any seat is already held or sold.
     */
    public LockToken holdSeats(String showId, List<String> seatIds, String userId) {
        Show show = requireShow(showId);
        validateSeatIds(show, seatIds);

        // Fast fail on already-sold seats before we touch the lock map (optimization only).
        for (String seatId : seatIds) {
            if (show.showSeat(seatId).isBooked()) {
                throw new SeatsUnavailableException(showId, seatId);
            }
        }

        // Atomically claim the holds (this is the real gate).
        lockProvider.lockSeats(showId, seatIds, userId, holdTtl);

        // Close the race with a booking that finished + released between the check above and
        // the claim: a seat can only be sold by a prior lock holder, so if it's booked now,
        // that happened before we held the lock. Roll back and fail.
        for (String seatId : seatIds) {
            if (show.showSeat(seatId).isBooked()) {
                lockProvider.unlockSeats(showId, seatIds, userId);
                throw new SeatsUnavailableException(showId, seatId);
            }
        }

        LockToken token = new LockToken(showId, seatIds, userId, Instant.now().plus(holdTtl));
        observers.forEach(o -> o.onSeatsHeld(showId, seatIds, userId));
        return token;
    }

    /**
     * Phase 2 — validate the hold, take payment, and finalize. On success the
     * seats become permanently booked and the holds are released; on payment
     * failure the holds are released and {@link PaymentFailedException} is thrown.
     */
    public Booking confirmBooking(LockToken token, PaymentStrategy paymentStrategy) {
        Show show = requireShow(token.showId());

        // The token is not trusted on its own — re-validate live ownership.
        if (token.isExpired() || !lockProvider.isValidLock(token.showId(), token.seatIds(), token.userId())) {
            throw new InvalidLockException("Hold expired or not owned by " + token.userId()
                    + " for seats " + token.seatIds());
        }

        List<Seat> seats = new ArrayList<>();
        for (String seatId : token.seatIds()) {
            seats.add(show.showSeat(seatId).seat());
        }

        long amount = pricingStrategy.priceFor(show, seats);
        PaymentResult payment = paymentStrategy.pay(amount);

        if (!payment.success()) {
            lockProvider.unlockSeats(token.showId(), token.seatIds(), token.userId());
            throw new PaymentFailedException("Payment declined for " + token.userId()
                    + "; seats released: " + token.seatIds());
        }

        // Payment ok: flip the durable booked flag while we still own the hold, THEN release.
        for (String seatId : token.seatIds()) {
            show.showSeat(seatId).markBooked();
        }
        lockProvider.unlockSeats(token.showId(), token.seatIds(), token.userId());

        Booking booking = new Booking("BKG-" + bookingSeq.incrementAndGet(), show, seats,
                token.userId(), amount, payment.transactionId(), BookingStatus.CONFIRMED);
        observers.forEach(o -> o.onBookingConfirmed(booking));
        return booking;
    }

    // ---- internals ----

    private void reapExpiredHolds() {
        List<ExpiredHold> expired = lockProvider.reapExpired();
        for (ExpiredHold hold : expired) {
            observers.forEach(o -> o.onHoldExpired(hold));
        }
    }

    private Show requireShow(String showId) {
        Show show = showsById.get(showId);
        if (show == null) throw new IllegalArgumentException("No such show: " + showId);
        return show;
    }

    private void validateSeatIds(Show show, List<String> seatIds) {
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("No seats requested");
        }
        if (seatIds.stream().distinct().count() != seatIds.size()) {
            throw new IllegalArgumentException("Duplicate seats in request: " + seatIds);
        }
        for (String seatId : seatIds) {
            if (show.showSeat(seatId) == null) {
                throw new IllegalArgumentException("Seat '" + seatId + "' not on show " + show.id());
            }
        }
    }
}
