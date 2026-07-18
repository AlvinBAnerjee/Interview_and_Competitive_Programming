package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model;

/**
 * A single settlement instruction produced by debt simplification: {@code from} should pay
 * {@code cents} to {@code to}. Applying every transaction leaves all balances at zero.
 */
public record Transaction(String from, String to, long cents) {

    @Override
    public String toString() {
        return from + " pays " + to + " " + Money.format(cents);
    }
}
