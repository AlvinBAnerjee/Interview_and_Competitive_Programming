package _1_BitMagic;

import java.util.Arrays;
import java.util.Scanner;

//Its an application of xor
public class OddOccurringNumber {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int val=0;
        int arr[]=new int[n];
        for (int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
            val=val^arr[i];
        }
        System.out.println(Arrays.toString(arr));
        System.out.println("The odd occuring value is:"+val);

    }
}
