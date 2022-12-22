package X_CompanyQuestions.MathWorks;

import java.util.Arrays;
import java.util.Scanner;

public class BeautifulArrangements {
    int count;
    public int countArrangement(int n) {
        count=0;
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=i+1;
        }
        rec(arr,0);
        return count;
    }
    public void rec(int arr[], int i)
    {
        if(i==arr.length)
        {
            count++;
            return;
        }
        for(int k=i;k<arr.length;k++)
        {
            int a=arr[i];
            int b=arr[k];
            arr[i]=b;
            arr[k]=a;
            if(arr[i]%(i+1) ==0|| (i+1)%arr[i]==0 )
            {
                rec(arr,i+1);
            }
            arr[i]=a;
            arr[k]=b;
        }

    }

}