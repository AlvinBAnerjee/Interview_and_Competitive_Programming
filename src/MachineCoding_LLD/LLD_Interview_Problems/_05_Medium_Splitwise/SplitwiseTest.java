package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.balance.DebtSimplifier;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.factory.SplitFactory;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.SplitType;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Transaction;

/**
 * Dependency-free harness (plain {@code main}, no JUnit — matching this repo). Each check prints
 * PASS/FAIL; the process exits non-zero if anything fails so {@code &&} chains / CI catch it.
 *
 * Covers: all three splits with exact-to-the-cent rounding, balance queries, settle-up, the
 * debt-simplification algorithm (fewer transactions + settles to zero), input validation, and — the
 * headline — concurrent {@code addExpense} conservation with no lost/double updates.
 */
public final class SplitwiseTest {

    private static int failures = 0;

    public static void main(String[] args) throws InterruptedException {
        testEqualSplitRoundsExactly();
        testPercentageSplitLargestRemainder();
        testExactSplitValidation();
        testBalancesAfterEqualExpense();
        testSettleUpReducesDebt();
        testDebtSimplificationChain();
        testSimplifiedSettlesToZero();
        testInvalidInputsRejected();
        testConcurrentAddExpenseNoLostUpdates();
        testConcurrentMixedConservation();

        System.out.println();
        if (failures == 0) {
            System.out.println("ALL TESTS PASSED ✅");
        } else {
            System.out.println(failures + " TEST(S) FAILED ❌");
            System.exit(1);
        }
    }

    private static void testEqualSplitRoundsExactly() {
        Map<String, Long> shares = SplitFactory.create(SplitType.EQUAL)
                .computeShares(1000, valued(0.0, "a", "b", "c")); // 1000¢ / 3
        long sum = shares.values().stream().mapToLong(Long::longValue).sum();
        check("equal split sums to total & distributes the odd cent",
                sum == 1000 && shares.get("a") == 334 && shares.get("b") == 333 && shares.get("c") == 333,
                shares.toString());
    }

    private static void testPercentageSplitLargestRemainder() {
        Map<String, Double> pct = new HashMap<>();
        pct.put("a", 33.33);
        pct.put("b", 33.33);
        pct.put("c", 33.34);
        Map<String, Long> shares = SplitFactory.create(SplitType.PERCENTAGE).computeShares(10, pct); // 10¢
        long sum = shares.values().stream().mapToLong(Long::longValue).sum();
        check("percentage split sums to total (largest remainder gets the cent)",
                sum == 10 && shares.get("c") == 4, shares.toString());
    }

    private static void testExactSplitValidation() {
        boolean ok = SplitFactory.create(SplitType.EXACT)
                .computeShares(2000, Map.of("a", 12.00, "b", 8.00)).values()
                .stream().mapToLong(Long::longValue).sum() == 2000;
        boolean rejects = throwsIae(() ->
                SplitFactory.create(SplitType.EXACT).computeShares(2000, Map.of("a", 12.00, "b", 5.00)));
        check("exact split validates the total", ok && rejects, "ok=" + ok + " rejects=" + rejects);
    }

    private static void testBalancesAfterEqualExpense() {
        SplitwiseService svc = trip();
        svc.addExpense("trip", "amy", 30.00, SplitType.EQUAL, valued(0.0, "amy", "bob", "cam"));
        Map<String, Long> amy = svc.getBalances("amy");
        check("equal expense: bob & cam each owe amy $10",
                amy.get("bob") == 1000 && amy.get("cam") == 1000, amy.toString());
    }

    private static void testSettleUpReducesDebt() {
        SplitwiseService svc = trip();
        svc.addExpense("trip", "amy", 30.00, SplitType.EQUAL, valued(0.0, "amy", "bob", "cam"));
        svc.settleUp("trip", "bob", "amy", 10.00);
        Map<String, Long> amy = svc.getBalances("amy");
        check("settle-up clears bob's debt to amy",
                !amy.containsKey("bob") && amy.get("cam") == 1000, amy.toString());
    }

    /** A→B→C→D chain of $10 debts nets to "A owes D $10" — one transaction, not three. */
    private static void testDebtSimplificationChain() {
        SplitwiseService svc = chain();
        List<Transaction> txns = svc.simplifyDebts("g");
        boolean single = txns.size() == 1;
        Transaction t = txns.get(0);
        check("debt chain simplifies to a single transaction A→D",
                single && t.from().equals("a") && t.to().equals("d") && t.cents() == 1000,
                txns.toString());
    }

    /** Applying the simplified transactions must zero out every balance. */
    private static void testSimplifiedSettlesToZero() {
        // Net: A -30, B +10, C -20, D +40  (sums to 0)
        Map<String, Long> net = new HashMap<>();
        net.put("a", -3000L);
        net.put("b", 1000L);
        net.put("c", -2000L);
        net.put("d", 4000L);

        List<Transaction> txns = new DebtSimplifier().simplify(net);
        Map<String, Long> after = new HashMap<>(net);
        for (Transaction t : txns) {
            after.merge(t.from(), t.cents(), Long::sum);  // debtor pays → their negative rises toward 0
            after.merge(t.to(), -t.cents(), Long::sum);   // creditor received → their positive falls toward 0
        }
        boolean allZero = after.values().stream().allMatch(v -> v == 0);
        boolean fewEnough = txns.size() <= 3; // <= n-1 for 4 users
        check("simplified transactions settle everyone to zero", allZero && fewEnough,
                "txns=" + txns.size() + " residual=" + after);
    }

    private static void testInvalidInputsRejected() {
        SplitwiseService svc = trip();
        boolean unknownGroup = throwsIae(() ->
                svc.addExpense("nope", "amy", 10, SplitType.EQUAL, valued(0.0, "amy")));
        boolean nonMember = throwsIae(() ->
                svc.addExpense("trip", "zzz", 10, SplitType.EQUAL, valued(0.0, "zzz")));
        boolean nonPositive = throwsIae(() ->
                svc.addExpense("trip", "amy", 0.0, SplitType.EQUAL, valued(0.0, "amy")));
        boolean badPct = throwsIae(() ->
                svc.addExpense("trip", "amy", 10, SplitType.PERCENTAGE, Map.of("amy", 50.0, "bob", 40.0)));
        check("invalid inputs rejected", unknownGroup && nonMember && nonPositive && badPct,
                "grp=" + unknownGroup + " mem=" + nonMember + " amt=" + nonPositive + " pct=" + badPct);
    }

    /**
     * THE conservation test. Members {A..E}; many threads each add "A pays $1.00 split equally 5
     * ways", so B,C,D,E each owe A 20¢ per expense. After N expenses every one of them must owe A
     * exactly 20×N (no lost/double updates), the ledger must conserve to zero, and every expense
     * must be recorded.
     */
    private static void testConcurrentAddExpenseNoLostUpdates() throws InterruptedException {
        SplitwiseService svc = new SplitwiseService();
        List<String> members = List.of("A", "B", "C", "D", "E");
        for (String m : members) {
            svc.addUser(m, m);
        }
        svc.createGroup("g", "Group", Set.copyOf(members));

        int threads = 32;
        int perThread = 200;
        int total = threads * perThread;
        Map<String, Double> split = valued(0.0, "A", "B", "C", "D", "E");

        runConcurrently(threads, perThread, () -> svc.addExpense("g", "A", 1.00, SplitType.EQUAL, split));

        long expectedEach = 20L * total; // 100¢ / 5 = 20¢ each, times N expenses
        Map<String, Long> amy = svc.getGroupBalances("g", "A");
        boolean exact = amy.get("B") == expectedEach && amy.get("C") == expectedEach
                && amy.get("D") == expectedEach && amy.get("E") == expectedEach;

        check("concurrent addExpense: no lost updates (exact per-pair totals)", exact, amy.toString());
        check("concurrent addExpense: ledger conserves to zero",
                svc.group("g").balances().netSum() == 0, "netSum != 0");
        check("concurrent addExpense: every expense recorded",
                svc.group("g").expenseCount() == total, "count=" + svc.group("g").expenseCount());
    }

    /** Random payers, concurrent — balances must still conserve to zero and lose no expense. */
    private static void testConcurrentMixedConservation() throws InterruptedException {
        SplitwiseService svc = new SplitwiseService();
        List<String> members = List.of("u0", "u1", "u2", "u3", "u4");
        for (String m : members) {
            svc.addUser(m, m);
        }
        svc.createGroup("g", "Group", Set.copyOf(members));
        Map<String, Double> split = valued(0.0, "u0", "u1", "u2", "u3", "u4");

        int threads = 16;
        int perThread = 300;
        AtomicInteger seq = new AtomicInteger();
        runConcurrently(threads, perThread, () -> {
            String payer = members.get(seq.getAndIncrement() % members.size());
            svc.addExpense("g", payer, 3.75, SplitType.EQUAL, split);
        });

        check("mixed concurrent expenses conserve to zero",
                svc.group("g").balances().netSum() == 0, "netSum != 0");
        check("mixed concurrent expenses all recorded",
                svc.group("g").expenseCount() == threads * perThread,
                "count=" + svc.group("g").expenseCount());
    }

    // ---- helpers ----

    private static SplitwiseService trip() {
        SplitwiseService svc = new SplitwiseService();
        svc.addUser("amy", "Amy");
        svc.addUser("bob", "Bob");
        svc.addUser("cam", "Cam");
        svc.createGroup("trip", "Trip", Set.of("amy", "bob", "cam"));
        return svc;
    }

    private static SplitwiseService chain() {
        SplitwiseService svc = new SplitwiseService();
        for (String u : List.of("a", "b", "c", "d")) {
            svc.addUser(u, u);
        }
        svc.createGroup("g", "Chain", Set.of("a", "b", "c", "d"));
        svc.addExpense("g", "b", 10.00, SplitType.EXACT, Map.of("a", 10.00));
        svc.addExpense("g", "c", 10.00, SplitType.EXACT, Map.of("b", 10.00));
        svc.addExpense("g", "d", 10.00, SplitType.EXACT, Map.of("c", 10.00));
        return svc;
    }

    /** Build a split-input map: every id mapped to the same value (values ignored for EQUAL). */
    private static Map<String, Double> valued(double value, String... ids) {
        Map<String, Double> m = new HashMap<>();
        for (String id : ids) {
            m.put(id, value);
        }
        return m;
    }

    private static void runConcurrently(int threads, int perThread, Runnable task) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch startGun = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try {
                    startGun.await();
                    for (int i = 0; i < perThread; i++) {
                        task.run();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        startGun.countDown();
        done.await();
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
    }

    private static boolean throwsIae(Runnable r) {
        try {
            r.run();
            return false;
        } catch (IllegalArgumentException expected) {
            return true;
        }
    }

    private static void check(String name, boolean ok, String actual) {
        System.out.printf("[%s] %s%s%n", ok ? "PASS" : "FAIL", name, ok ? "" : "  -> got: " + actual);
        if (!ok) {
            failures++;
        }
    }
}
