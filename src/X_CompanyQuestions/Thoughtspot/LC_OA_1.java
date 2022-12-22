package X_CompanyQuestions.Thoughtspot;

import java.util.Arrays;
import java.util.Scanner;

public class LC_OA_1 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        int mat[][]=new int[n][n];

        for(int x[]:mat)
        {
            Arrays.fill(x,Integer.MAX_VALUE);
        }
        for(int i=0;i<n;i++)
        {
            mat[i][i]=0;
        }



        for(int i=0;i<m;i++)
        {
            int u=sc.nextInt();
            int v=sc.nextInt();
            int w=sc.nextInt();
            mat[u-1][v-1]=Math.min(w,mat[u-1][v-1]);
            mat[v-1][u-1]=Math.min(w,mat[u-1][v-1]);
        }
        int s=sc.nextInt()-1;
        for(int x[]:mat)
        {
            System.out.println(Arrays.toString(x));
        }
        int dp[]=new int[n];

        for(int k=0;k<n;k++)
        {
            for(int i=0;i<n;i++)
            {
                for (int j=0;j<n;j++)
                {
                    if(mat[i][k]!=Integer.MAX_VALUE && mat[k][j]!=Integer.MAX_VALUE && i!=j && k!=i && k!=j)
                    {
                        if(mat[i][k]+mat[k][j] < mat[i][j])
                        {
                            mat[i][j]=mat[i][k]+mat[k][j];
                            if(i==0)dp[j]=dp[k]+1;
                        }
                        else if(mat[i][k]+mat[k][j] == mat[i][j])
                        {
                            if(i==0)dp[j]++;
                        }
                    }
                }
            }
        }

        for(int x[]:mat)
        {
            System.out.println(Arrays.toString(x));
        }
        System.out.println();
        System.out.println(Arrays.toString(dp));


    }
}
/*
4
4
1 2 1
2 3 1
1 4 1
4 3 1
1
 */