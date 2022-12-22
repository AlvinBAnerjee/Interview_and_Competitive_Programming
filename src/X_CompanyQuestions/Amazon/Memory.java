package X_CompanyQuestions.Amazon;

import java.util.Scanner;

public class Memory {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        int m=sc.nextInt();
        int sum=0;
        int max=0;
        for(int i=0;i<m;i++)
        {
            sum+=arr[i];
        }
        max=Math.max(sum, max);
        for(int i=m;i<n;i++)
        {
            sum=sum-arr[i-m]+arr[i];
            max=Math.max(max, sum);
        }
        int t=0;
        for(int x: arr)
        {
            t+=x;
        }
        System.out.println(t-max);
    }
}
