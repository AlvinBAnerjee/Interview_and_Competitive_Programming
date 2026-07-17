package _3_Arrays;

import java.util.Scanner;

/*
Approach: Find the second largest element in a single traversal.
Below is the complete algorithm for doing this:

1) Initialize two variables first and second to INT_MIN as
   first = second = INT_MIN
2) Start traversing the array,
   a) If the current element in array say arr[i] is greater
      than first. Then update first and second as,
      second = first
      first = arr[i]
   b) If the current element is in between first and second,
      then update second to store the value of current variable as
      second = arr[i]
3) Return the value stored in second.
 */
public class _1_Second_Largest_Element {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for ( int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        System.out.println(find_no(arr));
    }
    static int find_no(int arr[])
    {
        int n= (arr.length);
        int highest=Integer.MIN_VALUE;
        int second=Integer.MIN_VALUE;
        for (int i=0;i<n;i++)
        {
            if (arr[i]>highest)
            {
                second=highest;
                highest=arr[i];
            }
            else if (arr[i]>second&&arr[i]<highest)
            {
                second=arr[i];
            }
        }
        return second;
    }
}
