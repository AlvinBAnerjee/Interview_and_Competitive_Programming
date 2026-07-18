package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model;

/**
 * How an expense is divided among participants. Each maps to a {@code SplitStrategy}; the value of
 * the {@code Map<String,Double>} split input is interpreted per type (ignored for EQUAL, exact
 * dollar amounts for EXACT, percentages for PERCENTAGE).
 */
public enum SplitType {
    EQUAL,
    EXACT,
    PERCENTAGE
}
