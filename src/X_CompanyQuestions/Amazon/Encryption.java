package X_CompanyQuestions.Amazon;

import java.sql.Array;
import java.util.Arrays;
import java.util.Scanner;

public class Encryption {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        System.out.println(Arrays.toString(arr));
        for(int i=0;i<n-2;i++)
        {

            arr=reduce(arr);
            System.out.println(Arrays.toString(arr));
        }
    }
    public  static int[] reduce(int arr[])
    {
        int n=arr.length;
        int ans[]=new int[n-1];
        for(int i=0;i<n-1;i++)
        {
            ans[i]=(arr[i]+arr[i+1])%10;
        }
        return ans;
    }
}
