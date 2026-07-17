package _18_DP;
/*
refer gfg for the problems and solution and please please please watch the video made by pep code
 */
public class _10_Optimal_Strategy {
    public static void main(String[] args) {
        int arr1[] = { 8, 15, 3, 7 };
        int arr2[] = { 2, 2, 2, 2 };
        int arr3[] = { 20, 30, 2, 2, 2, 10 };
        System.out.println(findMaxValueRec(arr1,0,arr1.length-1));
        System.out.println(findMaxValueRec(arr2,0,arr2.length-1));
        System.out.println(findMaxValueRec(arr3,0,arr3.length-1));
        System.out.println("****************");
        System.out.println(findMaxValueDP(arr1));
        System.out.println(findMaxValueDP(arr2));
        System.out.println(findMaxValueDP(arr3));

    }
    static int findMaxValueRec(int arr[],int i,int j)
    {
        if (i+1==j)
            return Math.max(arr[i],arr[j]);
        else {
            int a = arr[i] + Math.min(findMaxValueRec(arr, i + 2, j), findMaxValueRec(arr, i + 1, j - 1));
            int b = arr[j] + Math.min(findMaxValueRec(arr,    i,j-2), findMaxValueRec(arr,i+1,j-1));
            return Math.max(a,b);
        }

    }
    static  int findMaxValueDP(int arr[])
    {
        int dp[][]=new int[arr.length][arr.length];
        for (int i=0;i<arr.length-1;i++)
            dp[i][i+1]=Math.max(arr[i],arr[i+1]);
        for (int gap=3;gap<arr.length;gap=gap+2)
        {
            for (int i=0;i+gap<arr.length;i++)
            {
                int j=i+gap;
                dp[i][j]=Math.max((arr[i]+Math.min(dp[i+1][j-1]
                                                  ,dp[i+2][j]))
                        ,         (arr[j]+Math.min(dp[i][j-2],
                                                   dp[i+1][j-1])));
            }
        }
        return dp[0][arr.length-1];
    }
}
