package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.balance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Transaction;

/**
 * Minimizes the number of settlement payments. Individual debts ("A owes B, B owes C, …") collapse
 * to each person's <b>net</b> position; then we only need to move money from net debtors to net
 * creditors. The greedy algorithm:
 *
 * <ol>
 *   <li>Split users into <b>creditors</b> (net &gt; 0) and <b>debtors</b> (net &lt; 0), each in a
 *       max-heap by magnitude.</li>
 *   <li>Repeatedly match the largest creditor with the largest debtor, settling
 *       {@code min(theirAmounts)} in one transaction; push back whatever remains.</li>
 *   <li>Stop when both heaps are empty (they must empty together — total credit = total debt,
 *       because balances conserve to zero).</li>
 * </ol>
 *
 * This yields <b>at most n − 1</b> transactions for n involved users — far fewer than the raw
 * pairwise debts. It is the standard, near-optimal answer; the <em>absolute</em> minimum is
 * NP-hard (a subset-sum/partition problem), which is worth stating out loud rather than pretending
 * greedy is provably optimal. Deterministic: ties break by user id.
 */
public final class DebtSimplifier {

    private record Party(String id, long amount) { }

    private static final Comparator<Party> BY_AMOUNT_DESC =
            Comparator.comparingLong(Party::amount).reversed().thenComparing(Party::id);

    public List<Transaction> simplify(Map<String, Long> netBalances) {
        PriorityQueue<Party> creditors = new PriorityQueue<>(BY_AMOUNT_DESC);
        PriorityQueue<Party> debtors = new PriorityQueue<>(BY_AMOUNT_DESC);

        netBalances.forEach((id, net) -> {
            if (net > 0) {
                creditors.add(new Party(id, net));
            } else if (net < 0) {
                debtors.add(new Party(id, -net)); // store magnitude
            }
        });

        List<Transaction> settlements = new ArrayList<>();
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            Party creditor = creditors.poll();
            Party debtor = debtors.poll();
            long settled = Math.min(creditor.amount, debtor.amount);

            settlements.add(new Transaction(debtor.id, creditor.id, settled));

            long creditorLeft = creditor.amount - settled;
            long debtorLeft = debtor.amount - settled;
            if (creditorLeft > 0) {
                creditors.add(new Party(creditor.id, creditorLeft));
            }
            if (debtorLeft > 0) {
                debtors.add(new Party(debtor.id, debtorLeft));
            }
        }
        return settlements;
    }
}
