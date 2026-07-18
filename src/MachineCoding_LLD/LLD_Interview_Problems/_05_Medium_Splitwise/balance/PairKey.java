package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.balance;

/**
 * A canonical, order-independent key for a pair of users. {@code of("bob","amy")} and
 * {@code of("amy","bob")} produce the <em>same</em> key with {@code low="amy"}, {@code high="bob"}.
 *
 * <p>This is the trick that makes the balance sheet lock-free: because each pair maps to exactly
 * <b>one</b> map entry, the debt between two users is a single signed number that can be updated
 * with one atomic {@code merge} — there's no "update A's row and B's row together" to coordinate,
 * hence no multi-lock, no deadlock. The stored value's sign is defined relative to {@code low}
 * (see {@code BalanceSheet}).
 */
public record PairKey(String low, String high) {

    public static PairKey of(String a, String b) {
        if (a.equals(b)) {
            throw new IllegalArgumentException("a user cannot owe themselves: " + a);
        }
        return a.compareTo(b) <= 0 ? new PairKey(a, b) : new PairKey(b, a);
    }
}
