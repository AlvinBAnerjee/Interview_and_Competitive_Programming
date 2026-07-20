package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.pricing;

import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Seat;
import MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model.Show;

import java.util.List;

/**
 * Price each seat as {@code show.basePrice × seat.category.multiplier}. Integer
 * paise throughout (rounded per seat) to avoid floating-point money drift.
 *
 * A different policy — weekend surge, dynamic demand pricing, coupons — is just
 * another {@link PricingStrategy} the service is constructed with; nothing else
 * changes.
 */
public class CategoryPricingStrategy implements PricingStrategy {
    @Override
    public long priceFor(Show show, List<Seat> seats) {
        long total = 0;
        for (Seat seat : seats) {
            total += Math.round(show.basePricePaise() * seat.category().multiplier());
        }
        return total;
    }
}
