package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model;

/**
 * Money is handled as <b>{@code long} cents</b> everywhere internally — never {@code double}.
 * Integer minor units give exact arithmetic (no float drift), cheap atomic updates, and the
 * invariant that split shares sum <em>exactly</em> to the total. The public API accepts dollars as
 * a {@code double} for convenience; this is the only place we convert, at the boundary.
 */
public final class Money {

    private Money() {
    }

    /** Dollars → cents, rounded to the nearest cent. */
    public static long cents(double dollars) {
        return Math.round(dollars * 100.0);
    }

    /** Cents → a "$12.34" string (handles negatives). */
    public static String format(long cents) {
        long abs = Math.abs(cents);
        String sign = cents < 0 ? "-" : "";
        return sign + "$" + (abs / 100) + "." + String.format("%02d", abs % 100);
    }
}
