package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise;

import java.util.List;
import java.util.Map;
import java.util.Set;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Money;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.SplitType;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Transaction;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.observer.LoggingExpenseObserver;

/**
 * A scripted, non-interactive walkthrough (like the other problems' {@code Main}s): the three split
 * types with exact rounding, balance queries, a settlement, and the debt-simplification algorithm
 * collapsing a chain of debts into fewer payments. For correctness + the concurrency conservation
 * test, see {@code SplitwiseTest}.
 */
public final class Main {

    public static void main(String[] args) {
        splitTypesAndBalances();
        separator();
        debtSimplification();
    }

    /** Equal / exact / percentage splits, a balance query, and a settle-up. */
    private static void splitTypesAndBalances() {
        System.out.println("=== Splits, balances, settle-up (group: trip) ===\n");
        SplitwiseService svc = new SplitwiseService();
        svc.addObserver(new LoggingExpenseObserver());
        svc.addUser("amy", "Amy");
        svc.addUser("bob", "Bob");
        svc.addUser("cam", "Cam");
        svc.createGroup("trip", "Road Trip", Set.of("amy", "bob", "cam"));

        // Amy pays $30 gas, split equally 3 ways → bob & cam each owe amy $10.
        svc.addExpense("trip", "amy", 30.00, SplitType.EQUAL, equalAmong("amy", "bob", "cam"));

        // Bob pays $20 lunch, exact: bob $0, amy $12, cam $8.
        svc.addExpense("trip", "bob", 20.00, SplitType.EXACT, Map.of("amy", 12.00, "cam", 8.00));

        // Cam pays $10 snacks, percentage: amy 50%, bob 50%.
        svc.addExpense("trip", "cam", 10.00, SplitType.PERCENTAGE, Map.of("amy", 50.0, "bob", 50.0));

        System.out.println("\nBalances (positive = they owe you):");
        for (String u : List.of("amy", "bob", "cam")) {
            System.out.println("  " + u + ": " + format(svc.getBalances(u)));
        }

        System.out.println("\nBob settles $10 to Amy...");
        svc.settleUp("trip", "bob", "amy", 10.00);
        System.out.println("  bob now: " + format(svc.getBalances("bob")));
    }

    /** A chain of debts collapses to fewer payments via net balances + greedy matching. */
    private static void debtSimplification() {
        System.out.println("=== Debt simplification (chain → fewer transactions) ===\n");
        SplitwiseService svc = new SplitwiseService();
        for (String u : List.of("a", "b", "c", "d")) {
            svc.addUser(u, u.toUpperCase());
        }
        svc.createGroup("g", "Chain", Set.of("a", "b", "c", "d"));

        // Build a chain: A owes B $10, B owes C $10, C owes D $10  (via exact expenses).
        svc.addExpense("g", "b", 10.00, SplitType.EXACT, Map.of("a", 10.00)); // B paid, A owes B
        svc.addExpense("g", "c", 10.00, SplitType.EXACT, Map.of("b", 10.00)); // C paid, B owes C
        svc.addExpense("g", "d", 10.00, SplitType.EXACT, Map.of("c", 10.00)); // D paid, C owes D

        System.out.println("Raw debts: A→B $10, B→C $10, C→D $10  (3 transactions)");
        List<Transaction> simplified = svc.simplifyDebts("g");
        System.out.println("Simplified to " + simplified.size() + " transaction(s):");
        simplified.forEach(t -> System.out.println("  " + t));
    }

    private static Map<String, Double> equalAmong(String... ids) {
        // For EQUAL the values are ignored; the keys are the participants.
        return java.util.Arrays.stream(ids).collect(java.util.stream.Collectors.toMap(i -> i, i -> 0.0));
    }

    private static String format(Map<String, Long> balances) {
        StringBuilder sb = new StringBuilder("{");
        balances.forEach((k, v) -> sb.append(k).append("=").append(Money.format(v)).append(" "));
        return sb.append("}").toString();
    }

    private static void separator() {
        System.out.println("\n======================================================\n");
    }
}
