package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.SplitType;

/**
 * Split the total evenly. Because cents don't always divide evenly, the leftover cents
 * ({@code total % n}) are handed out one each to the first participants <b>in sorted id order</b> —
 * deterministic, and the shares sum to exactly the total (e.g. 1000¢ / 3 → 334, 333, 333).
 */
public final class EqualSplitStrategy implements SplitStrategy {

    @Override
    public Map<String, Long> computeShares(long totalCents, Map<String, Double> input) {
        List<String> participants = new ArrayList<>(input.keySet());
        if (participants.isEmpty()) {
            throw new IllegalArgumentException("need at least one participant");
        }
        participants.sort(null); // deterministic remainder distribution

        int n = participants.size();
        long base = totalCents / n;
        long remainder = totalCents - base * n; // 0 .. n-1 extra cents

        Map<String, Long> shares = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            shares.put(participants.get(i), base + (i < remainder ? 1 : 0));
        }
        return shares;
    }

    @Override
    public SplitType type() {
        return SplitType.EQUAL;
    }
}
