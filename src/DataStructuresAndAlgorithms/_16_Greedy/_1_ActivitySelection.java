package _16_Greedy;

import java.util.Arrays;

public class _1_ActivitySelection {
    public int solve(int[][] A) {
        Arrays.sort(A, (a, b) -> {
            return a[1]-b[1];
        });
        int prev=Integer.MIN_VALUE;
        int count=0;

        for(int arr[] : A)
        {
            int start=arr[0];
            int end=arr[1];

            if(prev<start)
            {
                prev=end;
                count++;
            }
        }
        return count;
    }
}
