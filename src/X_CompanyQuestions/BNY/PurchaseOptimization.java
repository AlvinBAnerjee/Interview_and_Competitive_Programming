package X_CompanyQuestions.BNY;

import java.util.Scanner;

public class PurchaseOptimization {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        long arr[]=new long[n+1];
        long sum=0;
        for(int i=1;i<=n;i++)
        {
            sum+=sc.nextInt();
            arr[i]=sum;
        }

        int q=sc.nextInt();

        while(q-->0)
        {
            int pos=sc.nextInt();
            int amount=sc.nextInt();
            //pos--;
            int low=pos;
            int high=n;
            int max=0;
            while(low<=high)
            {
                int mid=(low+high)/2;
                if(arr[mid]-arr[pos-1]<=amount)
                {
                    //System.out.println(mid + " "+pos);
                    max=mid-pos+1;
                    low=mid+1;
                }
                else
                {
                    high=mid-1;
                }
            }
            System.out.println(max);
        }
    }
}
/*
5
3 4 5 5 7
3
2 10
1 24
5 5
 */