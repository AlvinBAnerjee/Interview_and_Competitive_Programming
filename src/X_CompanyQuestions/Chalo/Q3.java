package X_CompanyQuestions.Chalo;

import java.util.Arrays;
import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int t=sc.nextInt();
        while (t-->0)
        {
            int n=sc.nextInt();
            int m=sc.nextInt();
            int d=sc.nextInt();
            int arr[]=new int[n];
            int sum[]=new int[n];
            for(int i=0;i<n;i++)
            {
                arr[i]=sc.nextInt();
            }
            for(int i=0;i<n;i++)
            {
                for(int j=Math.max(0, i-d); j<=Math.min(n-1, i+d); j++)
                {
                    sum[j]+=arr[i];
                }
            }
            //System.out.println(Arrays.toString(sum));
            while(m-->0)
            {
                getMin(sum, d, n);
            }
            //System.out.println(Arrays.toString(sum));
            int min=Integer.MAX_VALUE;
            for(int x: sum)
                min=Math.min(x, min);
            System.out.println(min);
            //int min=getMin(sum,d,n);
        }
    }
    static int getMin(int sum[], int d, int n)
    {
        int min=Integer.MAX_VALUE;
        int global_freq=0;
        int index=-1;
        for(int i=d;i<=n-1-d;i++)
        {
            int x=Integer.MAX_VALUE;
            for(int j=i-d;j<=i+d;j++)
            {
                x=Math.min(sum[j],x);
            }
            int freq=0;
            for(int j=i-d;j<=i+d;j++)
            {
                if(sum[j]==x)
                {
                    freq++;
                }
            }
            if (x< min)
            {
                min=x;
                index=i;
                global_freq=freq;
            }
            else if(x==min)
            {
                if(global_freq< freq)
                {
                    global_freq=freq;
                    index=i;
                }
            }

            //System.out.print(x+":"+freq+",");
        }
        int i=index;
        for(int j=i-d;j<=i+d;j++)
        {
            sum[j]++;
        }
        //System.out.println(Arrays.toString(sum));
        return 0;
    }
/*

1
5 2 1
1 2 4 5 0
 */
}
