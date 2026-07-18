package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy;

import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.SplitType;

/**
 * How an expense is divided — the pluggable algorithm (Equal / Exact / Percentage). The participants
 * are the keys of {@code input}; the values are interpreted per type. A new split type is a new
 * class here plus one factory line — the core service never changes (OCP).
 *
 * <p><b>Rounding contract:</b> the returned per-participant shares must sum to <em>exactly</em>
 * {@code totalCents} — no lost or phantom cents. Implementations distribute any leftover cent
 * deterministically.
 *
 * <p>Pattern reference: {@code DesignPatterns/_10_StrategyDesignPattern}.
 */
public interface SplitStrategy {

    /**
     * @param totalCents the expense total in cents
     * @param input      participant id → type-specific value (ignored / exact dollars / percentage)
     * @return participant id → cents owed, summing exactly to {@code totalCents}
     */
    Map<String, Long> computeShares(long totalCents, Map<String, Double> input);

    SplitType type();
}
