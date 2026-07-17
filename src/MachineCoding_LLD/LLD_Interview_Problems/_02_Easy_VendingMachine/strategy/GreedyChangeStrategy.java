package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.strategy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Denomination;

/**
 * Greedy change-maker: hand out the largest denomination that fits, repeat. Correct and
 * optimal for a canonical coin system (…1, 2, 5, 10…); if it can't reach the exact amount
 * with the coins on hand it reports failure rather than over/under-paying.
 *
 * <p><b>Known limit (say this in the interview):</b> greedy is <i>not</i> optimal for
 * arbitrary denominations (classic counter-example: coins {1, 3, 4}, amount 6 → greedy
 * 4+1+1, optimal 3+3). That's exactly why this is a Strategy — drop in a DP maker when the
 * denominations aren't canonical.
 */
public class GreedyChangeStrategy implements ChangeStrategy {

    @Override
    public Optional<List<Denomination>> makeChange(int amount, Map<Denomination, Integer> available) {
        if (amount < 0) {
            return Optional.empty();
        }
        List<Denomination> plan = new ArrayList<>();
        Map<Denomination, Integer> remainingCoins = new EnumMap<>(Denomination.class);
        remainingCoins.putAll(available);

        int remaining = amount;
        for (Denomination d : Denomination.descending()) {
            int have = remainingCoins.getOrDefault(d, 0);
            while (remaining >= d.value && have > 0) {
                plan.add(d);
                remaining -= d.value;
                have--;
            }
        }
        return remaining == 0 ? Optional.of(plan) : Optional.empty();
    }
}
