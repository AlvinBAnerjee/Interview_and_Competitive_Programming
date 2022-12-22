package _8_Hashing;

import java.util.HashSet;
import java.util.Scanner;

public class _2_Subarray_with_given_sum {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        int sum=sc.nextInt();
        System.out.println(is_present(arr,n,sum));

    }
    static boolean is_present(int arr[],int n,int sum)
    {
        HashSet<Integer> set=new HashSet<>();
        set.add(0);
        int pre=0;
        for(int i=0;i<n;i++)
        {
            pre=pre+arr[i];
            if (set.contains(pre-sum))
                return true;
            set.add(pre);
        }
        return false;
    }
}
