package X_CompanyQuestions.Amazon;

import java.util.HashSet;
import java.util.Scanner;

public class AmazonParcel {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int k=sc.nextInt();
        HashSet<Integer> set=new HashSet<>();
        for(int i=0;i<n;i++)
        {
            set.add(sc.nextInt());
        }
        int sum=0;
        int i=1;
        k=k-n;
        while(k>0)
        {
            if(set.contains(i)) {
                i++;
                continue;

            }
            sum+=i;
            i++;
            k--;

        }
        System.out.println(sum);
    }
}
