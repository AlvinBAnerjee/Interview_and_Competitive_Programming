package X_CompanyQuestions.Intel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Prime {
    public static void main(String[] args) {
        int n=1000;
        int k=1;
        ArrayList<ArrayList<Integer>> map=new ArrayList<>();
        for(int i=0;i<=9;i++)
        {
            map.add(new ArrayList<>());
        }
        boolean notPrime[]=new boolean[100001];

        for(int i=2;i<=100000;i++)
        {
            if(notPrime[i]==true)
            {
                continue;
            }
            map.get(i%10).add(i);
            for(int j=2*i;j<=100000;j=j+i)
            {
                notPrime[j]=true;
            }


        }
        int sum=0;
        for(int x=0;x<n;x++)
        {
            sum+=map.get(k).get(x);
        }
        sum+=7;

        System.out.println(sum);



    }

    /*

     */
}
