package X_CompanyQuestions.BNY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BitProfit {
    static int findMSB1(int k)
    {
        for(int i=31;i>=0;i--)
        {
            if(( (k>>i)&1) ==1)
                return i;
        }
        return -1;
    }

//

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int k=sc.nextInt();
        int in[]=new int[n];
        int p[]=new int[n];
        for(int i=0;i<n;i++)
        {
            in[i]=sc.nextInt();
        }
        for(int i=0;i<n;i++)
        {
            p[i]=sc.nextInt();
        }
       int sum1=0;
        int sum2=0;
        for(int i=0;i<n;i++)
        {
            if(in[i]==k || match(in[i],k))
            {
                if(match(in[i],k))
                sum1+=p[i];
            }
        }
        for(int i=0;i<n;i++)
        {
            if(in[i]<k)
            {
                sum2+=p[i];
            }
        }
        System.out.println(Math.max(sum1, sum2));


    }
    public static boolean match(int x, int k)
    {
        for(int i=0;i<32;i++)
        {
            if(((k>>i)==0)&&( (x>>i)==1))
            {
                return false;
            }
        }
        return true;
    }
    /*
    5
3
2 3 1 5 9
1 2 6 1 5

     */
//    public static void main(String[] args) {
//        Scanner sc=new Scanner(System.in);
//        int n=sc.nextInt();
//        int k=sc.nextInt();
//        int in[]=new int[n];
//        int p[]=new int[n];
//        for(int i=0;i<n;i++)
//        {
//            in[i]=sc.nextInt();
//        }
//        for(int i=0;i<n;i++)
//        {
//            p[i]=sc.nextInt();
//        }
//        int dp[][]=new int[n+1][2*k];
//        for(int arr[]:dp)
//            Arrays.fill(arr,-1);
//        System.out.println(rec(0,0,dp, in,p, k));
//    }
//    public static int rec(int i, int j, int dp[][], int in[], int p[], int k)
//    {
//        int n=in.length;
//        if(i==n)
//        {
//            if(j<=k)
//            {
//                return 0;
//            }
//            return  Integer.MIN_VALUE;
//        }
//        if(j>k)
//        {
//            return Integer.MIN_VALUE;
//        }
//        if((j|in[i])<=k)
//        return Math.max(rec(i+1, j|in[i],dp,in,p,k)+p[i], rec(i+1, j, dp,in,p,k));
//        return rec(i+1, j, dp,in,p,k);
//    }

}
