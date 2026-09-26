package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple counting store: how many of each key are on hand. Backs the machine's product
 * stock (keyed by product code).
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
}
