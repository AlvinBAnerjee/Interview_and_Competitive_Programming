package X_CompanyQuestions.Amazon;

import java.util.Arrays;
import java.util.Scanner;

public class Prime {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int k=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();

        }
        Arrays.sort(arr);
        int start=0;
        int count=1;
        for(int i=0;i<n;i++)
        {
            if(arr[i]-arr[start]>k)
            {
                start=i;
                count++;
            }
        }
        System.out.println(count);
    }
}
