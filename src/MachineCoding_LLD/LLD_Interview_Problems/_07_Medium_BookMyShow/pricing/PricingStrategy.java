package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.pricing;

import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Seat;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Show;

import java.util.List;

/** Pluggable pricing policy: total price (in paise) for a set of seats in a show. */
public interface PricingStrategy {
    long priceFor(Show show, List<Seat> seats);
}
