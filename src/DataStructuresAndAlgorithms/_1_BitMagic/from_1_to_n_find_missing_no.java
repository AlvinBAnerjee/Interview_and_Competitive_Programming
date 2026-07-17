package _1_BitMagic;

import java.util.Arrays;
import java.util.Scanner;

public class from_1_to_n_find_missing_no {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int val=0;
        for (int i=1;i<=n;i++)
        {
            val=val^i;
        }
        int arr[]=new int[n-1];
        for (int i=0;i<n-1;i++)
        {
            arr[i]=sc.nextInt();
            val=val^arr[i];

        }
        System.out.println(Arrays.toString(arr));
        System.out.println("The missing no is:"+val);
    }
}
