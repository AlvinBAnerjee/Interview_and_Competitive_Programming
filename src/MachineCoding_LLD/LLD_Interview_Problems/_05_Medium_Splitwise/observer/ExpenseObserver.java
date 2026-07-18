package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.observer;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Expense;

/**
 * Observer notified when an expense is added — the seam for "email the group", "push a
 * notification", or "append to an audit log", without the service knowing who's listening.
 *
 * <p>Pattern reference: {@code DesignPatterns/_11_ObserverDesignPattern}.
 */
@FunctionalInterface
public interface ExpenseObserver {
    void onExpenseAdded(Expense expense);
}
