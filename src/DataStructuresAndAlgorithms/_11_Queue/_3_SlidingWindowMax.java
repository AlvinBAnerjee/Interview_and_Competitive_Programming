package _11_Queue;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class _3_SlidingWindowMax {
    public static void main(String[] args) {
        int arr[]={1,4,2,44,3,33,3,4,32,7,3,6};
        int k=3;
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(getMax(arr,k)));
    }
    static int [] getMax(int arr[],int k)
    {
        int ans[]=new int[arr.length-k+1];
        int index=0;
        Deque<Integer> dq=new LinkedList<>();
        dq.addFirst(0);
        for (int i=0;i<k;i++)
        {
            while (!dq.isEmpty()&&arr[dq.peekLast()]<=arr[i])
            {
                dq.removeLast();
            }
            dq.addLast(i);
        }
        for (int i=k;i<arr.length;i++)
        {
            ans[index++]=arr[dq.peekFirst()];
            while (!dq.isEmpty()&&dq.peekFirst()<=i-k)
            {
                dq.removeFirst();
            }
            while (!dq.isEmpty()&&arr[dq.peekLast()]<=arr[i])
            {
                dq.removeLast();
            }
            dq.addLast(i);
        }
        ans[index++]=arr[dq.peekFirst()];

        return ans;
    }
}
