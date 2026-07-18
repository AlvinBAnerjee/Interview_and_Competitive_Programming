package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.SplitType;

/**
 * Split by percentage (input values are percentages that must sum to 100). Rounding uses the
 * <b>largest-remainder method</b>: give everyone the floor of their exact share, then hand the few
 * leftover cents to the participants whose fractional part was largest. This keeps the split fair
 * <em>and</em> exactly equal to the total (plain rounding could drift a cent).
 */
public final class PercentageSplitStrategy implements SplitStrategy {

    private static final double EPSILON = 1e-6;

    @Override
    public Map<String, Long> computeShares(long totalCents, Map<String, Double> input) {
        if (input.isEmpty()) {
            throw new IllegalArgumentException("need at least one participant");
        }
        double pctSum = input.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(pctSum - 100.0) > EPSILON) {
            throw new IllegalArgumentException("percentages sum to " + pctSum + ", must be 100");
        }

        // Floor everyone, remember the fractional remainders.
        List<String> ids = new ArrayList<>(input.keySet());
        ids.sort(null);

        Map<String, Long> shares = new LinkedHashMap<>();
        List<Frac> fractions = new ArrayList<>();
        long assigned = 0;
        for (String id : ids) {
            double exact = totalCents * input.get(id) / 100.0;
            long floor = (long) Math.floor(exact);
            shares.put(id, floor);
            fractions.add(new Frac(id, exact - floor));
            assigned += floor;
        }

        // Distribute the leftover cents to the largest fractional parts (ties by id).
        long leftover = totalCents - assigned;
        fractions.sort(Comparator.comparingDouble(Frac::frac).reversed().thenComparing(Frac::id));
        for (int i = 0; i < leftover; i++) {
            String id = fractions.get(i).id();
            shares.merge(id, 1L, Long::sum);
        }
        return shares;
    }

    private record Frac(String id, double frac) { }

    @Override
    public SplitType type() {
        return SplitType.PERCENTAGE;
    }
}
