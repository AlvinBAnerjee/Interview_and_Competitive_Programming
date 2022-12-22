package X_CompanyQuestions.Navi;

import java.util.Arrays;
import java.util.Scanner;

public class WarLane {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int l=sc.nextInt();
        int arr[]=new int[n+2];
        arr[0]=0;
        arr[n+1]=n;
        for(int i=1;i<=n;i++)
        {
            arr[i]=sc.nextInt();
        }
        Arrays.sort(arr);
        double max=Integer.MIN_VALUE;
        for(int i=1;i<=n+1;i++)
        {
            double diff=arr[i]-arr[i-1];
            //System.out.println(diff);
            max=Math.max(diff/2,max);
        }
        System.out.println((int)Math.ceil(max));
    }
}
