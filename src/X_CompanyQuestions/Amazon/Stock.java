package X_CompanyQuestions.Amazon;

import java.util.Scanner;

public class Stock {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        int prefix[]=new int[n];
        int sum=0;
        for(int i=0;i<n;i++)
        {
            sum=sum+arr[i];
            prefix[i]=sum;
        }
        int min=-1;
        int minv=Integer.MAX_VALUE;
        for(int i=0;i<n-1;i++)
        {
            int a=(prefix[i]/(i+1));
            int b=(prefix[n-1]-prefix[i])/(n-i-1);
            int temp=Math.abs(a-b);
            System.out.println(temp);
            if(temp< minv)
            {
                minv=temp;
                min=i;
            }
        }
        System.out.println(min+1);
    }
}
