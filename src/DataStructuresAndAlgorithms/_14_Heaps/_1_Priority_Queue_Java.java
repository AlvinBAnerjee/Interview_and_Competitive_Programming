package _14_Heaps;

import java.util.Collections;
import java.util.PriorityQueue;

public class _1_Priority_Queue_Java {

    public static void main(String[] args) {
        PriorityQueue<Integer> pq=new PriorityQueue<>();//PriorityQueue<Integer> queue = new PriorityQueue<>(10, Collections.reverseOrder());

        pq.add(10);
        pq.add(-1);
        pq.add(100);

        System.out.println(pq.remove());
        System.out.println(pq.remove());
        System.out.println(pq.remove());

    }
}
