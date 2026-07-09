package _6_Sorting;


import java.util.*;

public class _5_TreeSet_TreeMapCheatSheet {

    public static void main(String[] args) {

        // ─── TreeSet ───────────────────────────────────────────
        TreeSet<Integer> ts = new TreeSet<>(Arrays.asList(1, 3, 5, 7, 9));
        System.out.println("Set: " + ts);            // [1, 3, 5, 7, 9]

        System.out.println(ts.first());              // 1  — smallest
        System.out.println(ts.last());               // 9  — greatest

        System.out.println(ts.floor(4));            // 3  — greatest ≤ 4
        System.out.println(ts.floor(5));            // 5  — exact match ok
        System.out.println(ts.ceiling(4));          // 5  — smallest ≥ 4

        System.out.println(ts.lower(5));            // 3  — strictly < 5
        System.out.println(ts.higher(5));           // 7  — strictly > 5

        System.out.println(ts.lower(1));            // null — nothing below 1
        System.out.println(ts.higher(9));           // null — nothing above 9

        System.out.println(ts.headSet(5));          // [1, 3]     exclusive
        System.out.println(ts.tailSet(5));          // [5, 7, 9]  inclusive
        System.out.println(ts.subSet(3, 8));        // [3, 5, 7]  [from, to)
        System.out.println(ts.descendingSet());      // [9, 7, 5, 3, 1]

        System.out.println(ts.pollFirst());          // 1, set=[3,5,7,9]
        System.out.println(ts.pollLast());           // 9, set=[3,5,7]

        // Custom comparator (reverse order)
        TreeSet<Integer> rev = new TreeSet<>(Comparator.reverseOrder());
        rev.addAll(Arrays.asList(1, 3, 5));
        System.out.println(rev.first());             // 5 — "first" in reverse order

        // ─── TreeMap ───────────────────────────────────────────
        TreeMap<Integer, String> tm = new TreeMap<>();
        tm.put(1, "one"); tm.put(3, "three");
        tm.put(5, "five"); tm.put(7, "seven");
        System.out.println(tm);

        // Key navigation
        System.out.println(tm.firstKey());           // 1
        System.out.println(tm.lastKey());            // 7
        System.out.println(tm.floorKey(4));         // 3
        System.out.println(tm.ceilingKey(4));       // 5
        System.out.println(tm.lowerKey(5));         // 3
        System.out.println(tm.higherKey(5));        // 7

        // Entry navigation — gets key AND value
        Map.Entry<Integer,String> e = tm.floorEntry(4);
        System.out.println(e.getKey() + " → " + e.getValue()); // 3 → three

        System.out.println(tm.firstEntry());         // 1=one
        System.out.println(tm.lastEntry());          // 7=seven

        // Poll — removes + returns
        System.out.println(tm.pollFirstEntry());     // 1=one, map shrinks
        System.out.println(tm.pollLastEntry());      // 7=seven

        // Submap views
        System.out.println(tm.headMap(5));          // {1=one, 3=three}
        System.out.println(tm.tailMap(3));          // {3=three, 5=five, 7=seven}
        System.out.println(tm.subMap(1, 6));        // {1=one, 3=three, 5=five}
        System.out.println(tm.descendingMap());      // {7=seven, 5=five, ...}

        // Iterating in sorted order
        for (Map.Entry<Integer, String> entry : tm.entrySet())
            System.out.println(entry.getKey() + " → " + entry.getValue());
    }
}
