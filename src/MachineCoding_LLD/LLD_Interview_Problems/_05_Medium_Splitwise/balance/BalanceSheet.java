package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.balance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Who owes whom, stored as one signed {@code long} (cents) per {@link PairKey}. For a key
 * {@code (low, high)} the value {@code v} means <b>{@code low} owes {@code high} v cents</b>
 * (negative ⇒ {@code high} owes {@code low}). One entry captures the full relationship between two
 * users, so it stays antisymmetric by construction.
 *
 * <h3>Thread-safety without locks</h3>
 * Every mutation is a single {@link ConcurrentMap#merge merge} on one entry — an atomic
 * read-modify-write — so two expenses touching the same pair can't lose an update, and expenses on
 * different pairs never contend. Because a debt lives in <em>one</em> entry (not two rows to keep in
 * sync), there is <b>no multi-key update</b> to coordinate, and therefore <b>no lock ordering / no
 * deadlock</b> to worry about. The self-balancing entries also mean the grand total of all balances
 * is <b>always exactly zero</b>, at every instant, for any interleaving. See {@code SOLUTION.md} §5.
 */
public final class BalanceSheet {

    private final ConcurrentMap<PairKey, Long> ledger = new ConcurrentHashMap<>();

    /** Record that {@code debtor} now owes {@code creditor} an additional {@code cents}. */
    public void record(String debtor, String creditor, long cents) {
        adjust(debtor, creditor, cents);
    }

    /** Record a payment: {@code payer} paid {@code payee}, reducing what payer owes them. */
    public void settle(String payer, String payee, long cents) {
        adjust(payer, payee, -cents);
    }

    /** Core atomic update: "{@code from} owes {@code to}" changes by {@code signedCents}. */
    private void adjust(String from, String to, long signedCents) {
        if (signedCents == 0) {
            return;
        }
        PairKey key = PairKey.of(from, to);
        // Store relative to `low`; flip the sign when `from` is actually the `high` user.
        long delta = key.low().equals(from) ? signedCents : -signedCents;
        // Atomic merge; collapse the entry to absent when it nets to zero (keeps the map compact).
        ledger.merge(key, delta, (existing, d) -> {
            long sum = existing + d;
            return sum == 0 ? null : sum;
        });
    }

    /** How much {@code debtor} currently owes {@code creditor} (negative ⇒ the reverse). */
    public long owedBy(String debtor, String creditor) {
        PairKey key = PairKey.of(debtor, creditor);
        Long v = ledger.get(key);
        if (v == null) {
            return 0;
        }
        return key.low().equals(debtor) ? v : -v;
    }

    /** For one user: counterparty → net cents that counterparty owes them (positive = owed to me). */
    public Map<String, Long> balancesFor(String user) {
        Map<String, Long> out = new HashMap<>();
        ledger.forEach((key, v) -> {
            if (key.low().equals(user)) {
                out.merge(key.high(), -v, Long::sum); // user=low owes high v ⇒ high owes user -v
            } else if (key.high().equals(user)) {
                out.merge(key.low(), v, Long::sum);   // low owes user(high) v
            }
        });
        out.values().removeIf(v -> v == 0);
        return out;
    }

    /** Net position of every user: userId → net cents (positive = is owed, negative = owes). */
    public Map<String, Long> netBalances() {
        Map<String, Long> out = new HashMap<>();
        ledger.forEach((key, v) -> {
            out.merge(key.high(), v, Long::sum);   // high is owed v
            out.merge(key.low(), -v, Long::sum);   // low owes v
        });
        out.values().removeIf(v -> v == 0);
        return out;
    }

    /**
     * Sum of every user's net balance — the conservation invariant, which must be exactly {@code 0}
     * for any interleaving of concurrent writes. Handy for tests.
     */
    public long netSum() {
        return netBalances().values().stream().mapToLong(Long::longValue).sum();
    }
}
