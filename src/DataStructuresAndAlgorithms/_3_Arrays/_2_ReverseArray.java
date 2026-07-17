package _3_Arrays;

import java.util.Arrays;
import java.util.Scanner;

public class _2_ReverseArray {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for ( int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        System.out.println(Arrays.toString(arr));
        for (int i=0;i<=(n-1)/2;i++)
        {
            int t=arr[i];
            arr[i]=arr[n-1-i];
            arr[n-1-i]=t;
        }
        System.out.println(Arrays.toString(arr));

    }
}
