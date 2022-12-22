package X_CompanyQuestions.Microsoft;

import java.util.HashMap;
import java.util.Scanner;

public class SubarrayAverage {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int s=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt()-s;
        }
        System.out.println(solve(arr));
    }
    public static int solve(int arr[])
    {
        HashMap<Integer, Integer> map=new HashMap<>();
        map.put(0,1);
        int res=0;
        int sum=0;
        for(int i=0;i<arr.length;i++)
        {
            sum+=arr[i];
            if(map.containsKey(sum))
                res+=map.get(sum);
            if(!map.containsKey(sum))
            {
                map.put(sum,0);
            }
            map.put(sum, map.get(sum)+1);
        }
        return res;
    }
}
