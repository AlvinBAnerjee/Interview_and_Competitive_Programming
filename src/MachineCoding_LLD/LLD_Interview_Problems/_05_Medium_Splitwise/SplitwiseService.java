package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.balance.DebtSimplifier;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.factory.SplitFactory;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Expense;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Group;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Money;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.SplitType;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Transaction;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.User;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.observer.ExpenseObserver;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy.SplitStrategy;

/**
 * The Facade the app talks to: manage users/groups, add expenses (routed through the split
 * {@code Strategy} + {@code Factory}), settle up, query balances, and simplify debts. It holds no
 * balance math itself — that lives in each group's lock-free {@code BalanceSheet}.
 *
 * <p>Thread-safe: users/groups are in {@link ConcurrentHashMap}s and each expense is applied via
 * atomic per-pair merges, so many {@code addExpense}/{@code settleUp} calls run concurrently with
 * balances staying consistent (and conserving to zero). See {@code SOLUTION.md} §5.
 */
public final class SplitwiseService {

    private final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Group> groups = new ConcurrentHashMap<>();
    private final List<ExpenseObserver> observers = new CopyOnWriteArrayList<>();
    private final DebtSimplifier simplifier = new DebtSimplifier();
    private final AtomicLong expenseSeq = new AtomicLong();

    public void addObserver(ExpenseObserver observer) {
        observers.add(observer);
    }

    public User addUser(String id, String name) {
        User user = new User(id, name);
        if (users.putIfAbsent(id, user) != null) {
            throw new IllegalArgumentException("user already exists: " + id);
        }
        return user;
    }

    public Group createGroup(String groupId, String name, Set<String> memberIds) {
        for (String m : memberIds) {
            if (!users.containsKey(m)) {
                throw new IllegalArgumentException("unknown user in group: " + m);
            }
        }
        Group group = new Group(groupId, name, memberIds);
        if (groups.putIfAbsent(groupId, group) != null) {
            throw new IllegalArgumentException("group already exists: " + groupId);
        }
        return group;
    }

    /**
     * Add an expense: {@code payerId} paid {@code amount} (dollars), split among the participants in
     * {@code splitInput} per {@code type}. Each participant then owes the payer their share.
     */
    public Expense addExpense(String groupId, String payerId, double amount,
                              SplitType type, Map<String, Double> splitInput) {
        Group group = requireGroup(groupId);
        long totalCents = Money.cents(amount);
        if (totalCents <= 0) {
            throw new IllegalArgumentException("amount must be positive, got " + amount);
        }
        requireMember(group, payerId);
        if (splitInput.isEmpty()) {
            throw new IllegalArgumentException("expense needs at least one participant");
        }
        for (String participant : splitInput.keySet()) {
            requireMember(group, participant);
        }

        SplitStrategy strategy = SplitFactory.create(type);
        Map<String, Long> shares = strategy.computeShares(totalCents, splitInput);

        // Each participant owes the payer their share (the payer owes themselves nothing).
        shares.forEach((participant, share) -> {
            if (!participant.equals(payerId)) {
                group.balances().record(participant, payerId, share);
            }
        });

        Expense expense = new Expense("E-" + expenseSeq.incrementAndGet(), groupId, payerId,
                totalCents, type, shares, Instant.now());
        group.recordExpense(expense);
        observers.forEach(o -> o.onExpenseAdded(expense));
        return expense;
    }

    /** Record a settlement payment of {@code amount} dollars from {@code from} to {@code to}. */
    public void settleUp(String groupId, String from, String to, double amount) {
        Group group = requireGroup(groupId);
        requireMember(group, from);
        requireMember(group, to);
        long cents = Money.cents(amount);
        if (cents <= 0) {
            throw new IllegalArgumentException("settlement amount must be positive");
        }
        group.balances().settle(from, to, cents);
    }

    /**
     * Overall balances for a user across every group: counterparty → net cents that counterparty
     * owes the user (negative ⇒ the user owes them).
     */
    public Map<String, Long> getBalances(String userId) {
        Map<String, Long> total = new HashMap<>();
        for (Group group : groups.values()) {
            if (group.hasMember(userId)) {
                group.balances().balancesFor(userId).forEach((other, amt) -> total.merge(other, amt, Long::sum));
            }
        }
        total.values().removeIf(v -> v == 0);
        return total;
    }

    /** Balances for a user within one group. */
    public Map<String, Long> getGroupBalances(String groupId, String userId) {
        return requireGroup(groupId).balances().balancesFor(userId);
    }

    /** Minimize the settlements needed to zero out a group's debts. */
    public List<Transaction> simplifyDebts(String groupId) {
        Group group = requireGroup(groupId);
        return simplifier.simplify(group.balances().netBalances());
    }

    public Group group(String groupId) {
        return requireGroup(groupId);
    }

    private Group requireGroup(String groupId) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("unknown group: " + groupId);
        }
        return group;
    }

    private static void requireMember(Group group, String userId) {
        if (!group.hasMember(userId)) {
            throw new IllegalArgumentException("user " + userId + " is not in group " + group.id());
        }
    }
}
