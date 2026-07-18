package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.observer;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Expense;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.Money;

/** A concrete observer that prints each new expense — used by the {@code Main} walkthrough. */
public final class LoggingExpenseObserver implements ExpenseObserver {

    @Override
    public void onExpenseAdded(Expense expense) {
        System.out.println("    [expense] " + expense.payerId() + " paid "
                + Money.format(expense.amountCents()) + " (" + expense.type() + ") → " + expense.shares());
    }
}
