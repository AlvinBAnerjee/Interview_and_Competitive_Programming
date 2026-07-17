package _8_Hashing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class _0_Hash_Set_Java {
    public static void main(String[] args) {
        HashSet<Integer> set=new HashSet<>();
        set.add(1);
        set.add(6);
        set.add(6);
        set.add(8);
        set.add(9);
        System.out.println(set.size());
        System.out.println(set.contains(0));
        System.out.println(set.contains(9));
        Iterator<Integer> i =set.iterator();
        while (i.hasNext())
        {
            System.out.println(i.next());
        }

        for(int x: set)
        {
            System.out.println(x);
        }

    }
}
