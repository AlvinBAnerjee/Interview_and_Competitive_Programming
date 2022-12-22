package X_CompanyQuestions.flipkart;

import java.util.HashSet;
import java.util.Scanner;

public class Cooking {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        HashSet<Integer> left=new HashSet<>();
        HashSet<Integer> right=new HashSet<>();
        for(int  i=0;i<n;i++)
        {
            if(arr[i]>0)
            {
                if(left.contains(arr[i]))
                    right.add(arr[i]);
                else
                    left.add(arr[i]);
            }
            else
            {
                if(right.contains(arr[i]))
                    left.add(arr[i]);
                else
                    right.add(arr[i]);
            }
        }
        int sum=0;
        for(int x: left)
            sum=sum+x;
        for(int x: right)
            sum=sum-x;
        System.out.println(sum);
    }
}
