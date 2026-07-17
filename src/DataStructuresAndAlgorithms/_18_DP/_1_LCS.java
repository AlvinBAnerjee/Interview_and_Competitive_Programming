package _18_DP;

import java.util.Scanner;

public class _1_LCS {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str1=sc.nextLine();
        String str2=sc.nextLine();
        int m=str1.length();
        int n=str2.length();
        int lengths[][]=new int[m+1][n+1];
        for (int i=0;i<=m;i++)
        {
            for (int j=0;j<=n;j++)
            {
                if (i==0||j==0) {
                    lengths[i][j] = 0;
                }
                else if (str1.charAt(i-1)==str2.charAt(j-1))
                {
                    lengths[i][j]=1+lengths[i-1][j-1];
                }
                else
                {
                    lengths[i][j]=Math.max(lengths[i-1][j],lengths[i][j-1]);//exclude the last element from string 1 or string 2
                }
            }
        }

        for (int i=0;i<=m;i++)
        {
            for (int j=0;j<=n;j++)
            {
                System.out.print(lengths[i][j]+  "  ");
            }
            System.out.println();
        }
        System.out.println("The longest LCS is:");
        int i=m;
        int j=n;
        String ans="";
        while (i!=0&&j!=0)
        {
            if (str1.charAt(i-1)==str2.charAt(j-1))
            {
                ans=str1.charAt(i-1)+ans;
                i--;
                j--;
            }
            else
            {
                if (lengths[i-1][j]>lengths[i][j-1])
                {
                    i--;
                }
                else
                {
                    j--;
                }
            }
        }
        System.out.println(ans);

    }
}
