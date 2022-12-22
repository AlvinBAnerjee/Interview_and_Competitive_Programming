package X_CompanyQuestions.MathWorks;

import java.util.Arrays;
import java.util.Scanner;

public class DistinctProblems {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        int target=sc.nextInt();
        Arrays.sort(arr);
        int i=0;
        int j=n-1;
        while(i<j)
        {
            int sum=arr[i]+arr[j];
            if(sum==target)
            {
                System.out.println(arr[i] + " "+arr[j]);
                while(i<j && arr[i]==arr[i+1])i++;
                while(i<j && arr[j]==arr[j-1])j--;
                i++;
                j--;
            }
            else if(sum<target)
                i++;
            else j--;
        }
    }
}
