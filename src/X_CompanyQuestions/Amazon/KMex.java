package X_CompanyQuestions.Amazon;

import java.util.HashSet;
import java.util.Scanner;

public class KMex {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        HashSet<Integer> set=new HashSet<>();
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
            set.add(arr[i]);
        }
        int t=sc.nextInt();
        for(int k=0;k<1000;k++)
        {
            if(!set.contains(k))
            {
                t--;
                if(t==0) {
                    System.out.println(k);break;
                }
                //break;
            }
        }
    }
}
