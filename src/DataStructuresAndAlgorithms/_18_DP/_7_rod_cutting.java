package _18_DP;

import java.util.Arrays;
import java.util.Scanner;

public class _7_rod_cutting {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int a=10;
        int b=9;
        int c=7;
        int  n=11;
        System.out.println(findMaxCut(n,a,b,c));
    }
    static int findMaxCut(int n,int a,int b,int c)
    {
        int arr[]=new int[n+1];
        arr[0]=0;
        for (int i=1;i<=n;i++)
        {
            arr[i]=-1;
            if (i>=a)
                arr[i]=Math.max(arr[i],arr[i-a]);
            if (i>=b)
                arr[i]=Math.max(arr[i],arr[i-b]);
            if (i>=c)
                arr[i]=Math.max(arr[i],arr[i-c]);
            if (arr[i]!=-1)
                arr[i]++;

        }
        return arr[n];
    }

}
