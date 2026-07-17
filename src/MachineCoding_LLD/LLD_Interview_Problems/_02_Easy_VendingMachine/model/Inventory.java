package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple counting store: how many of each key are on hand. Used twice — once for product
 * stock (keyed by product code) and once for the machine's coin reserve (keyed by
 * {@link Denomination}) — so the counting/decrement logic lives in exactly one place.
 */
public class Inventory<K> {

    private final Map<K, Integer> counts = new LinkedHashMap<>();

    public void add(K key, int qty) {
        if (qty <= 0) {
            return;
        }
        counts.merge(key, qty, Integer::sum);
    }

    public void addOne(K key) {
        add(key, 1);
    }

    /** Remove a single unit. Returns false if there was nothing to remove. */
    public boolean removeOne(K key) {
        Integer c = counts.get(key);
        if (c == null || c <= 0) {
            return false;
        }
        if (c == 1) {
            counts.remove(key);
        } else {
            counts.put(key, c - 1);
        }
        return true;
    }

    public int count(K key) {
        return counts.getOrDefault(key, 0);
    }

    public boolean hasStock(K key) {
        return count(key) > 0;
    }

    /** Read-only view of the current counts (used to plan change without mutating). */
    public Map<K, Integer> snapshot() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(counts));
    }
}
