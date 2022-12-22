package X_CompanyQuestions.Amazon;

import java.util.Arrays;
import java.util.Scanner;

public class Alexa {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        int prefix[]=new int[n+1];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        for(int i=0;i<n;i++)
        {
            prefix[i+1]=prefix[i]+arr[i];
        }
        System.out.println(Arrays.toString(prefix));
        int max=0;
        for(int i=1;i<=n;i++)
        {
            //System.out.println("   ___   "+ prefix[i] + "   "+(prefix[n]-prefix[i-1]));
            int temp=Math.max(prefix[i], prefix[n]- prefix[i-1]);
            //System.out.println(temp);
            max=Math.max(max,temp);
        }
        System.out.println(max);
    }
}
