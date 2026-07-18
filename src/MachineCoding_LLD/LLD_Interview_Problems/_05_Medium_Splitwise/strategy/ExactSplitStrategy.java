package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Money;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.SplitType;

/**
 * Each participant owes an exact stated amount (input values are dollar amounts). The one rule:
 * the stated amounts must sum to exactly the total — otherwise the expense is inconsistent and we
 * reject it rather than silently absorb the difference.
 */
public final class ExactSplitStrategy implements SplitStrategy {

    @Override
    public Map<String, Long> computeShares(long totalCents, Map<String, Double> input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("need at least one participant");
        }
        Map<String, Long> shares = new LinkedHashMap<>();
        long sum = 0;
        for (Map.Entry<String, Double> e : input.entrySet()) {
            long c = Money.cents(e.getValue());
            if (c < 0) {
                throw new IllegalArgumentException("exact share for " + e.getKey() + " is negative");
            }
            shares.put(e.getKey(), c);
            sum += c;
        }
        if (sum != totalCents) {
            throw new IllegalArgumentException(
                    "exact shares sum to " + Money.format(sum) + " but total is " + Money.format(totalCents));
        }
        return shares;
    }

    @Override
    public SplitType type() {
        return SplitType.EXACT;
    }
}
