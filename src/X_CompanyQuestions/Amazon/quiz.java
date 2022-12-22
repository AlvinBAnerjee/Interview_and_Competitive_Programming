package X_CompanyQuestions.Amazon;

import java.util.PriorityQueue;
import java.util.Scanner;

public class quiz {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int ans[]=new int[n];
        int need[]=new int[n];
        int k=sc.nextInt();
        for(int i=0;i<n;i++)
        {
            ans[i]=sc.nextInt();
        }
        for(int i=0;i<n;i++)
        {
            need[i]=sc.nextInt();
        }
        PriorityQueue<Integer> pq=new PriorityQueue<>();
        int count=0;
        for(int i=0;i<n;i++)
        {
            pq.add(need[i]-ans[i]);
        }
        while(!pq.isEmpty())
        {
            int top=pq.remove();
            if(top> k)break;
            k=k-top;
            count++;
        }
        System.out.println(count);


    }
}
