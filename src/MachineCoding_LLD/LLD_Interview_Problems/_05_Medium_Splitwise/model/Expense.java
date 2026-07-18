package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model;

import java.time.Instant;
import java.util.Map;

/**
 * An immutable record of one expense: who paid, how much (cents), how it split, and when. Kept for
 * history/audit and passed to observers. The {@code shares} map (participant → cents owed for this
 * expense) is computed by the split strategy and always sums to {@code amountCents}.
 */
public record Expense(
        String id,
        String groupId,
        String payerId,
        long amountCents,
        SplitType type,
        Map<String, Long> shares,
        Instant createdAt) {

    public Expense {
        shares = Map.copyOf(shares); // defensive immutability
    }

    @Override
    public String toString() {
        return "Expense[" + id + " " + payerId + " paid " + Money.format(amountCents)
                + " (" + type + ") shares=" + shares + "]";
    }
}
