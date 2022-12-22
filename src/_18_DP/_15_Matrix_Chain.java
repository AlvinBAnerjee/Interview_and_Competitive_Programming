package _18_DP;
public class _15_Matrix_Chain {
    public static void main(String[] args) {
        int dimension_array[] = {20, 10, 30,40, 10, 30,40,20, 10, 30,40,100,34,23,34,56,78,98,45};
        System.out.println(findOptimalDP(dimension_array));

        System.out.println(findOptimalRec(dimension_array,0,dimension_array.length-1));


    }
    static int findOptimalRec(int dimension_array[],int i,int j)
    {
        /*
        say we have a matrix chain A1 x A2 x ........ x An
        now findOptimal (dimension_array,2,4) means (A3,A4)
         */
        if (i+1==j)
            return 0;
        int res=Integer.MAX_VALUE;
        for (int x=i+1;x<j;x++)
        {
            res=Math.min(res,findOptimalRec(dimension_array,i,x)+
                             findOptimalRec(dimension_array,x,j)+
                             dimension_array[i]*dimension_array[x]*dimension_array[j]);
        }
        return res;
    }
    static int findOptimalDP(int dimensional_array[])
    {
        int n=dimensional_array.length;
        int dp[][]=new int[n][n];
        for (int gap=1;gap<n;gap++)
        {
            for (int x=0; x+gap<n;x++)
            {
                if (gap==1)
                {
                    dp[x][x+1]=0;
                    continue;
                }
                int i=x;
                int j=i+gap;
                int res=Integer.MAX_VALUE;
                for (int y=i+1;y<j;y++)
                {
                    res=Math.min(res,dp[i][y]+
                            dp[y][j]+
                            dimensional_array[i]*dimensional_array[y]*dimensional_array[j]);
                }
                dp[i][j]=res;
            }
        }
        return dp[0][n-1];
    }

}
