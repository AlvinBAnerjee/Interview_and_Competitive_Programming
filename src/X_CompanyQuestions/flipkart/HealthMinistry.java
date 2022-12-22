package X_CompanyQuestions.flipkart;

import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HealthMinistry {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        PriorityQueue<Integer> pq=new PriorityQueue<>(Collections.reverseOrder());
        for(int x: arr)
        {
            pq.add(x);
        }
        int res=0;
        while (pq.size()>1)
        {
            int x=pq.remove();
            int y=pq.remove();
            res=res+2;
            if(x-1>0)pq.add(x-1);
            if(y-1>0)pq.add(y-1);

        }
        if(pq.size()==1)res++;
        System.out.println(res);
    }
}
