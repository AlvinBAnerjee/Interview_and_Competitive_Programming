package _3_Arrays;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

public class _5_Trapping_Rain {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for (int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        int max=-1;
        int lmax[]=new int[n];
        for (int i=0;i<n;i++)
        {
            if (arr[i]>max)
                max=arr[i];
            lmax[i]=max;
        }
        max=-1;
        int rmax[]=new int[n];
        for (int i=n-1;i>=0;i--)
        {
            if (arr[i]>max)
                max=arr[i];
            rmax[i]=max;
        }
        int res=0;
        for (int i=0;i<n;i++)
        {
            res=res+Math.min(lmax[i],rmax[i])-arr[i];
        }
        System.out.println(res);

    }

}
