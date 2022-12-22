package _8_Hashing;

import java.util.HashMap;
import java.util.Scanner;

public class _3_MaxLen_Subarray_with_given_sum {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int sum=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        System.out.println(find_length(arr,n,sum));
    }
    static int find_length(int arr[],int n,int sum)
    {
        int pre=0;
        int res=0;
        HashMap<Integer,Integer> map=new HashMap<>();
        for (int i=0;i<n;i++)
        {
            pre=pre+arr[i];
            if (pre==sum)
            {
                res=i+1;
            }
            if (map.containsKey(pre)==false)
            {
                map.put(pre,i);
                //System.out.println(arr[i]+" "+pre+" "+i);
            }
            if (map.containsKey(pre-sum))
            {
                res=Math.max(res,i-map.get(pre-sum));
            }
        }
        return res;
    }
}
