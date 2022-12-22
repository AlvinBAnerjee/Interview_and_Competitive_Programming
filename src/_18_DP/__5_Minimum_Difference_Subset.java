package _18_DP;

import java.util.Arrays;

public class __5_Minimum_Difference_Subset {
    public static void main(String[] args) {
        int elements[]={1,6,5,11};

        __5_Minimum_Difference_Subset ob=new __5_Minimum_Difference_Subset();
        ob.findMinDifference(elements.length,elements);
    }
    int findMinDifference(int item,int elements[])
    {
        int sum=0;
        for (int i=0;i<elements.length;i++)
        {
            sum=sum+elements[i];
        }
        int dp[][]=new int[item+1][sum+1];
        for (int arr[]:
                dp) {
            Arrays.fill(arr,-1);

        }

        subsetSumUtil(dp, sum, item, elements);

        for(int i=0;i<=item;i++)
        {
            System.out.println(Arrays.toString(dp[i]));
        }
        int min=Integer.MAX_VALUE;
        for (int i=0;i<=sum/2;i++)// gfg online sol refer
        {
            if (dp[item][i]==1)
            {
                int s1=i;//sum of partition 1
                int s2=sum-s1;/// sum of partition 2;
                int diff=Math.abs(s1-s2);//absolute difference
                min=Math.min(min,diff);
            }
        }
        return min;
    }
    void subsetSumUtil(int dp[][],int sum,int item,int elements[])
    {
        for (int i=1;i<=item;i++)
        {
            for (int j=1;j<=sum;j++)
            {
                if (i==0)
                {
                    dp[i][j]=0;
                    continue;

                }
                if (j==0)
                {
                    dp[i][j]=1;
                    continue;
                }
                if (j>=elements[i])
                {
                    dp[i][j]= dp[i-1][j-elements[i-1]] | dp[i-1][j];
                }
                else
                    dp[i][j]=dp[i-1][j];
            }
        }
    }
}
