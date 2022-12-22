package X_CompanyQuestions.Amazon;

import java.util.Arrays;
import java.util.Scanner;

public class Candies {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int k=sc.nextInt();
        int arr[]=new int[k];
        int low=1;
        int high=n;
        int candidate=0;
        while(low<=high)
        {
            int mid=(low+high)/2;
            int sum=mid*(mid+1)/2;
            if(sum<=n)
            {
                candidate=Math.max(candidate, mid/k);
                low=mid+1;
            }
            else
            {
                high=mid-1;
            }
        }
        int count=candidate;
        int last=count*k;
        n=n-last*(last+1)/2;
        int term = (count * k) + 1;
        int j=0;
        while (n > 0) {
            if (term <= n) {
                arr[j++] = term;
                n -= term;
                term++;
            }
            else {
                arr[j] += n;
                n = 0;
            }
        }
        for(int i=0;i<n;i++)
        {
            arr[i]+=(count*(i+1))+ (k*(count)*(count-1)/2);
        }
        System.out.println(Arrays.toString(arr));

    }
}
