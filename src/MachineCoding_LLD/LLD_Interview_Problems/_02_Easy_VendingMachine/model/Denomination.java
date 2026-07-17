package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The coins and notes the machine accepts. Modeling money as a fixed enum (rather than a
 * free int) means the machine can only ever see valid denominations, and lets us build
 * type-safe per-denomination maps with {@link java.util.EnumMap}.
 */
public enum Denomination {
    ONE(1), TWO(2), FIVE(5), TEN(10), TWENTY(20), FIFTY(50), HUNDRED(100);

    public final int value;

    Denomination(int value) {
        this.value = value;
    }

    /** Denominations from largest to smallest — the order a greedy change-maker walks. */
    public static List<Denomination> descending() {
        List<Denomination> all = new ArrayList<>(Arrays.asList(values()));
        all.sort((a, b) -> Integer.compare(b.value, a.value));
        return all;
    }
}
