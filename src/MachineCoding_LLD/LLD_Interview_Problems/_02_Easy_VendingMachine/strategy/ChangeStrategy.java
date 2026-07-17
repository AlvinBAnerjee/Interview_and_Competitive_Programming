package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model.Denomination;

/**
 * STRATEGY for making change. Pulled out because the algorithm is the part most likely to
 * change: greedy is fine for a canonical coin set, but arbitrary denominations need a
 * dynamic-programming maker — and you can swap that in without touching the state machine.
 *
 * <p>Pure by contract: it only <em>plans</em> the change against a read-only view of what's
 * available. The machine applies the plan. That keeps the strategy free of side effects and
 * lets the caller validate feasibility before committing anything.
 */
public interface ChangeStrategy {

    /**
     * @param amount    the change owed (may be 0)
     * @param available read-only counts of each denomination on hand
     * @return the coins to return, or empty if exact change is impossible
     */
    Optional<List<Denomination>> makeChange(int amount, Map<Denomination, Integer> available);
}
