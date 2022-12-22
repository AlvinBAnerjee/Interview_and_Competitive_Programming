package X_CompanyQuestions.Oracle;

import java.util.*;

public class ProcessExecution {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        HashMap<Integer, Integer> map=new HashMap<>();
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
            if(!map.containsKey(arr[i]))
            map.put(arr[i],0);
            map.put(arr[i], map.get(arr[i])+arr[i]);

        }
        HashMap<Integer, Integer> nmap=new HashMap<>();
        for(int x: map.keySet())
        {
            if(!map.containsKey(x-1))nmap.put(x-1,0);
            if(!map.containsKey(x+1))nmap.put(x+1,0);
            nmap.put(x, map.get(x));
        }
        int m=nmap.size();
        int temp[]=new int[m];
        int i=0;
        for(int x: nmap.keySet())
        {
            temp[i++]=x;
        }
        Arrays.sort(temp);
        for( i=0;i<temp.length;i++)
        {
            temp[i]=nmap.get(temp[i]);
        }
        System.out.println(Arrays.toString(temp));
        System.out.println(findMaxsubarrayDP(temp));
    }
    static int findMaxsubarrayDP(int arr[])
    {
        int n=arr.length;
        if (n==1)
            return arr[0];
        else if (n==2)
            return Math.max(arr[0],arr[1]);
        int dp[]=new int[n];
        dp[0]=arr[0];
        dp[1]=Math.max(arr[0],arr[1]);
        for (int i=2;i<n;i++)
        {
            dp[i]=Math.max(dp[i-1],dp[i-2]+arr[i]);
        }
        return dp[n-1];
    }
}
