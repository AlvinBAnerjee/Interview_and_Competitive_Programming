package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow;

import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.InMemorySeatLockProvider;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock.LockToken;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Booking;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.City;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Movie;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Screen;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Seat;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.SeatCategory;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Show;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Theatre;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.observer.LoggingBookingObserver;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.payment.CreditCardPayment;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.pricing.CategoryPricingStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Walks the happy path plus the two things this problem is really about:
 * double-booking prevention and hold expiry.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        BookMyShowService service = new BookMyShowService(
                new InMemorySeatLockProvider(),
                new CategoryPricingStrategy(),
                Duration.ofMillis(1500));            // short TTL so the expiry demo is quick
        service.addObserver(new LoggingBookingObserver());
        service.startExpiryReaper(Duration.ofMillis(300));

        Show show = sampleShow();
        service.addShow(show);
        String showId = show.id();

        System.out.println("=== 1. Search ===");
        service.searchShows("M1", "C1")
                .forEach(s -> System.out.printf("  %s @ %s (%s) show=%s%n",
                        s.movie().title(), s.theatre().name(), s.theatre().city().name(), s.id()));

        System.out.println("\n=== 2. Availability (all free) ===");
        System.out.println(service.getAvailability(showId).render());

        System.out.println("=== 3. Alice holds A1,A2 then pays ===");
        LockToken aliceToken = service.holdSeats(showId, List.of("A1", "A2"), "alice");
        System.out.println(service.getAvailability(showId).render());
        Booking booking = service.confirmBooking(aliceToken, new CreditCardPayment("4242"));
        System.out.println("  -> " + booking);
        System.out.println(service.getAvailability(showId).render());

        System.out.println("=== 4. Bob tries to grab A1 (already sold) ===");
        try {
            service.holdSeats(showId, List.of("A1"), "bob");
        } catch (SeatsUnavailableException e) {
            System.out.println("  rejected: " + e.getMessage());
        }

        System.out.println("\n=== 5. Carol holds B1 but never pays -> hold expires ===");
        service.holdSeats(showId, List.of("B1"), "carol");
        System.out.println("  B1 right after hold: " + service.getAvailability(showId).statusOf("B1"));
        Thread.sleep(1900);                          // wait past the 1500ms TTL (+ reaper tick)
        System.out.println("  B1 after expiry:     " + service.getAvailability(showId).statusOf("B1"));
        System.out.println("  Dave can now hold B1: " + service.holdSeats(showId, List.of("B1"), "dave").userId());

        service.shutdown();
    }

    /** One movie, one theatre, one 3×4 screen (row A=recliner, B=gold, C=silver). */
    private static Show sampleShow() {
        City city = new City("C1", "Bengaluru");
        Movie movie = new Movie("M1", "Inception", "English", 148);
        Screen screen = new Screen("SC1", "Audi 1", buildSeats());
        Theatre theatre = new Theatre("T1", "PVR Forum", city, List.of(screen));
        return new Show("SHOW1", movie, theatre, screen, LocalDateTime.now().plusHours(3), 20000); // ₹200 base
    }

    private static List<Seat> buildSeats() {
        List<Seat> seats = new ArrayList<>();
        String[] rowLabels = {"A", "B", "C"};
        SeatCategory[] rowCats = {SeatCategory.RECLINER, SeatCategory.GOLD, SeatCategory.SILVER};
        for (int r = 0; r < rowLabels.length; r++) {
            for (int c = 1; c <= 4; c++) {
                seats.add(new Seat(rowLabels[r] + c, r + 1, c, rowCats[r]));
            }
        }
        return seats;
    }
}
