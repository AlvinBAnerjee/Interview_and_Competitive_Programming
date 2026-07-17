package _18_DP;

import java.util.Arrays;

public class _8_Minimum_Coins_to_make_a_Value {
    public static void main(String[] args) {
        int coins[]={99,8};
        int val=30;
        System.out.println(findMin(val,coins));
    }
    static int findMin(int value,int coins[])
    {
        int n=coins.length;
        int arr[]=new int[value+1];
        for (int i=1;i<=value;i++)
        {
            arr[i]=Integer.MAX_VALUE;
            for (int j=0;j<n;j++)
            {
                if (coins[j]<=i)
                {
                    int res=arr[i-coins[j]];
                    if (res!=Integer.MAX_VALUE)
                    {
                        int min=Math.min(arr[i],res+1);
                        arr[i]=min;
                    }
                }
            }
        }
        return arr[value];
    }
}
