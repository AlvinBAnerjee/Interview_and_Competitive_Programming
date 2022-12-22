package _8_Hashing;

import java.util.HashSet;
import java.util.Scanner;

public class _1_Pair_with_given_sum_in_an_unsorted_Array {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        int sum=sc.nextInt();
        HashSet<Integer> set=new HashSet<>();
        for( int i =0;i<n;i++)
        {
            int rem=sum-arr[i];
            if (set.contains(rem)){
                System.out.println("True");
            }
            set.add(arr[i]);
        }
    }
}
