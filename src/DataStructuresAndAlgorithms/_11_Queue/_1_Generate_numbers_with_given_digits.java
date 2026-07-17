package _11_Queue;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
/*
set={5,6}
n=10
output={5,5,56,65,66,555,556,565,566}
 */
public class _1_Generate_numbers_with_given_digits {
    public static void main(String[] args) {
        int set[]={5,6};
        Arrays.sort(set);
        int n=100;
        Deque<Integer> deque=new LinkedList<>();
        deque.addLast(0);
        int count=0;
        int top=deque.removeFirst();
        while (count<n)
        {
            for (int j:set)
                deque.addLast(top*10+j);
            top=deque.removeFirst();
            System.out.println(top);
            count++;

        }
    }
}
