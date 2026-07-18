package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.balance.BalanceSheet;

/**
 * A group of users who share expenses. It owns the members, its own {@link BalanceSheet} (so debts
 * are scoped per group), and the expense history. Membership and history use concurrent collections
 * so reads during concurrent {@code addExpense} calls are safe.
 */
public final class Group {

    private final String id;
    private final String name;
    private final Set<String> memberIds = new CopyOnWriteArraySet<>();
    private final BalanceSheet balanceSheet = new BalanceSheet();
    private final ConcurrentLinkedQueue<Expense> expenses = new ConcurrentLinkedQueue<>();

    public Group(String id, String name, Set<String> memberIds) {
        this.id = id;
        this.name = name;
        this.memberIds.addAll(memberIds);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public boolean hasMember(String userId) {
        return memberIds.contains(userId);
    }

    public Set<String> members() {
        return Set.copyOf(memberIds);
    }

    public BalanceSheet balances() {
        return balanceSheet;
    }

    public void recordExpense(Expense expense) {
        expenses.add(expense);
    }

    public List<Expense> expenses() {
        return List.copyOf(expenses);
    }

    public int expenseCount() {
        return expenses.size();
    }
}
