package X_CompanyQuestions.Navi;

import java.util.Arrays;
import java.util.Scanner;

public class CleanVillage {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        int arr1[]=new int[m];
        int arr2[]=new int[m];
        for(int i=0;i<m;i++)
        {
            arr1[i]=sc.nextInt();
            arr2[i]=-arr1[i];
        }
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        int x=solve(arr1, n);
        int y=solve(arr2,n);
        System.out.println(Math.min(x,y));
    }
    static  int solve(int arr[], int n)
    {
        int m=arr.length;
        int prefix[]=new int[m];
        prefix[0]=Math.abs(arr[0]);
        for(int i=1;i<m;i++)
            prefix[i]=prefix[i-1]+Math.abs(arr[i]-arr[i-1]);
        int res=Integer.MAX_VALUE;
        for(int i=n-1;i<m;i++)
        {
            if(i==n-1)
            {
                res=Math.min(res, prefix[i]);
            }
            else
            {
                res=Math.min(res, prefix[i]-prefix[i-n+1] + Math.abs(arr[i-n+1]));
            }
        }
        return res;
    }
}
