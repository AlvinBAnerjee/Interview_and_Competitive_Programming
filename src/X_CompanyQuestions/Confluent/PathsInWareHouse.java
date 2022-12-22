package X_CompanyQuestions.Confluent;

import java.util.Scanner;

public class PathsInWareHouse {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        int arr[][]=new int[n][m];
        for(int i=0;i<n;i++)
        {
            for (int j=0;j<m;j++)
            {
                arr[i][j]=sc.nextInt();
            }
        }
        System.out.println(rec(arr,0, 0, n , m));
    }
    public static int rec(int arr[][], int i, int j, int n ,int m)
    {
        if(i<0 || i>=n || j<0 || j>=m)return 0;
        if(arr[i][j]==0)return  0;
        if(i==n-1 && j==m-1)
            return 1;
        return rec(arr, i+1, j, n, m)+rec(arr, i, j+1, n, m);
    }
}
