package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow;

import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.InMemorySeatLockProvider;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.LockToken;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Booking;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.BookingStatus;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.City;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Movie;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Screen;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Seat;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.SeatCategory;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.SeatStatus;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Show;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Theatre;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment.CreditCardPayment;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment.PaymentResult;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment.PaymentStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.pricing.CategoryPricingStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PASS/FAIL harness (plain main, no JUnit — matching this repo). Exits non-zero
 * on any failure. The headline is {@code concurrentSameSeat_exactlyOneWins}.
 */
public class BookMyShowTest {

    private static int passed = 0, failed = 0;

    public static void main(String[] args) throws Exception {
        holdThenConfirm_marksSeatBooked();
        cannotHold_alreadySoldSeat();
        cannotHold_seatHeldByAnother();
        multiSeat_isAllOrNothing();
        expiredHold_releasesSeatLazily();
        confirm_withExpiredToken_fails();
        paymentDeclined_releasesSeats();
        concurrentSameSeat_exactlyOneWins();
        concurrentDistinctSeats_allSucceed();

        System.out.printf("%n==== %d passed, %d failed ====%n", passed, failed);
        if (failed > 0) System.exit(1);
    }

    // ---- individual tests ----

    private static void holdThenConfirm_marksSeatBooked() {
        BookMyShowService svc = newService(Duration.ofSeconds(30));
        LockToken token = svc.holdSeats("SHOW1", List.of("A1"), "u1");
        check("held A1 -> status HELD", svc.getAvailability("SHOW1").statusOf("A1") == SeatStatus.HELD);
        Booking b = svc.confirmBooking(token, new CreditCardPayment("1111"));
        check("confirm -> CONFIRMED", b.status() == BookingStatus.CONFIRMED);
        check("A1 -> status BOOKED", svc.getAvailability("SHOW1").statusOf("A1") == SeatStatus.BOOKED);
        // recliner row A: base 20000 * 2.5 = 50000
        check("price = ₹500 (recliner)", b.amountPaise() == 50000);
        svc.shutdown();
    }

    private static void cannotHold_alreadySoldSeat() {
        BookMyShowService svc = newService(Duration.ofSeconds(30));
        svc.confirmBooking(svc.holdSeats("SHOW1", List.of("A1"), "u1"), new CreditCardPayment("1"));
        check("holding a sold seat throws", throwsUnavailable(() -> svc.holdSeats("SHOW1", List.of("A1"), "u2")));
        svc.shutdown();
    }

    private static void cannotHold_seatHeldByAnother() {
        BookMyShowService svc = newService(Duration.ofSeconds(30));
        svc.holdSeats("SHOW1", List.of("A1"), "u1");
        check("holding a held seat throws", throwsUnavailable(() -> svc.holdSeats("SHOW1", List.of("A1"), "u2")));
        svc.shutdown();
    }

    private static void multiSeat_isAllOrNothing() {
        BookMyShowService svc = newService(Duration.ofSeconds(30));
        svc.holdSeats("SHOW1", List.of("B2"), "u1");                         // pre-hold the middle seat
        boolean threw = throwsUnavailable(() -> svc.holdSeats("SHOW1", List.of("B1", "B2", "B3"), "u2"));
        check("multi-seat hold with one taken -> throws", threw);
        // rollback: B1 and B3 must still be free for someone else
        boolean b1b3Free = svc.getAvailability("SHOW1").statusOf("B1") == SeatStatus.AVAILABLE
                && svc.getAvailability("SHOW1").statusOf("B3") == SeatStatus.AVAILABLE;
        check("failed multi-seat hold rolled back B1 & B3", b1b3Free);
        svc.shutdown();
    }

    private static void expiredHold_releasesSeatLazily() throws InterruptedException {
        BookMyShowService svc = newService(Duration.ofMillis(300));          // no reaper started -> pure lazy
        svc.holdSeats("SHOW1", List.of("C1"), "u1");
        Thread.sleep(400);
        check("expired hold seen as AVAILABLE", svc.getAvailability("SHOW1").statusOf("C1") == SeatStatus.AVAILABLE);
        check("another user can hold after expiry", svc.holdSeats("SHOW1", List.of("C1"), "u2").userId().equals("u2"));
        svc.shutdown();
    }

    private static void confirm_withExpiredToken_fails() throws InterruptedException {
        BookMyShowService svc = newService(Duration.ofMillis(300));
        LockToken token = svc.holdSeats("SHOW1", List.of("A1"), "u1");
        Thread.sleep(400);                                                   // let the hold lapse
        boolean threw;
        try { svc.confirmBooking(token, new CreditCardPayment("1")); threw = false; }
        catch (InvalidLockException e) { threw = true; }
        check("confirm with expired token throws InvalidLockException", threw);
        check("seat not booked after failed confirm", svc.getAvailability("SHOW1").statusOf("A1") != SeatStatus.BOOKED);
        svc.shutdown();
    }

    private static void paymentDeclined_releasesSeats() {
        BookMyShowService svc = newService(Duration.ofSeconds(30));
        LockToken token = svc.holdSeats("SHOW1", List.of("A1"), "u1");
        PaymentStrategy declining = amount -> PaymentResult.declined();
        boolean threw;
        try { svc.confirmBooking(token, declining); threw = false; }
        catch (PaymentFailedException e) { threw = true; }
        check("declined payment throws PaymentFailedException", threw);
        check("seat released after decline", svc.getAvailability("SHOW1").statusOf("A1") == SeatStatus.AVAILABLE);
        check("someone else can now book it", svc.holdSeats("SHOW1", List.of("A1"), "u2").userId().equals("u2"));
        svc.shutdown();
    }

    /** THE test: N threads race for the same seat; exactly one wins, N-1 fail cleanly. */
    private static void concurrentSameSeat_exactlyOneWins() throws InterruptedException {
        BookMyShowService svc = newService(Duration.ofSeconds(30));
        int threads = 64;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger wins = new AtomicInteger();
        AtomicInteger unavailable = new AtomicInteger();
        AtomicInteger other = new AtomicInteger();

        for (int i = 0; i < threads; i++) {
            final String user = "u" + i;
            pool.execute(() -> {
                ready.countDown();
                try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                try {
                    svc.holdSeats("SHOW1", List.of("A1"), user);
                    wins.incrementAndGet();
                } catch (SeatsUnavailableException e) {
                    unavailable.incrementAndGet();
                } catch (RuntimeException e) {
                    other.incrementAndGet();
                }
            });
        }
        ready.await();
        go.countDown();                                                      // fire all at once
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);

        check("exactly ONE thread held the seat", wins.get() == 1);
        check("the other " + (threads - 1) + " got SeatsUnavailable", unavailable.get() == threads - 1);
        check("no unexpected exceptions", other.get() == 0);
        check("seat is HELD afterwards", svc.getAvailability("SHOW1").statusOf("A1") == SeatStatus.HELD);
        svc.shutdown();
    }

    /** N threads each grab a DISTINCT seat -> all succeed, no false contention. */
    private static void concurrentDistinctSeats_allSucceed() throws InterruptedException {
        BookMyShowService svc = newService(Duration.ofSeconds(30));
        List<String> seatIds = new ArrayList<>();
        for (String row : List.of("A", "B", "C")) for (int c = 1; c <= 4; c++) seatIds.add(row + c);
        int n = seatIds.size();                                             // 12
        ExecutorService pool = Executors.newFixedThreadPool(n);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger wins = new AtomicInteger();
        for (String seatId : seatIds) {
            pool.execute(() -> {
                try { go.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                try { svc.holdSeats("SHOW1", List.of(seatId), "user-" + seatId); wins.incrementAndGet(); }
                catch (RuntimeException ignored) { }
            });
        }
        go.countDown();
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        check("all " + n + " distinct-seat holds succeeded", wins.get() == n);
        svc.shutdown();
    }

    // ---- helpers ----

    private static boolean throwsUnavailable(Runnable r) {
        try { r.run(); return false; }
        catch (SeatsUnavailableException e) { return true; }
    }

    private static BookMyShowService newService(Duration ttl) {
        BookMyShowService svc = new BookMyShowService(
                new InMemorySeatLockProvider(), new CategoryPricingStrategy(), ttl);
        svc.addShow(sampleShow());
        return svc;
    }

    private static Show sampleShow() {
        City city = new City("C1", "Bengaluru");
        Movie movie = new Movie("M1", "Inception", "English", 148);
        List<Seat> seats = new ArrayList<>();
        String[] rows = {"A", "B", "C"};
        SeatCategory[] cats = {SeatCategory.RECLINER, SeatCategory.GOLD, SeatCategory.SILVER};
        for (int r = 0; r < rows.length; r++)
            for (int c = 1; c <= 4; c++)
                seats.add(new Seat(rows[r] + c, r + 1, c, cats[r]));
        Screen screen = new Screen("SC1", "Audi 1", seats);
        Theatre theatre = new Theatre("T1", "PVR Forum", city, List.of(screen));
        return new Show("SHOW1", movie, theatre, screen, LocalDateTime.now().plusHours(3), 20000);
    }

    private static void check(String name, boolean ok) {
        if (ok) { passed++; System.out.println("[PASS] " + name); }
        else    { failed++; System.out.println("[FAIL] " + name); }
    }
}
