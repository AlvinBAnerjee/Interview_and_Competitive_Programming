package X_CompanyQuestions.Thoughtspot;

import java.util.Scanner;

public class LC_OA_2 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int parent[]=new int[n];
        for(int i=0;i<n;i++)
        {
            parent[i]=sc.nextInt()-1;
        }
        int c=sc.nextInt();
        int d[]=new int[c];
        int e[]=new int[c];
        for(int i=0;i<c;i++)
        {
            d[i]=sc.nextInt();
        }
        //System.out.println("*****");
        for(int i=0;i<c;i++)
        {
            e[i]=sc.nextInt();
        }
        for(int i=0;i<c;i++)
        {
            int ans=0;
            int a=d[i]-1;

            int b=e[i];
            ans=Math.max(ans, (a+1)^b);
            while(parent[a]!=a)
            {
                a=parent[a];
                ans=Math.max(ans, (a+1)^b);
                //a=parent[a];
            }
            System.out.println(ans);
        }
    }
}
