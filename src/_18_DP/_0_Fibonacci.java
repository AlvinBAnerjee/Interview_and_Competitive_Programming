package _18_DP;
import java.util.Scanner;
/*
Dynamic Programming is mainly an optimization over plain recursion.
Wherever we see a recursive solution that has repeated calls for same inputs,
we can optimize it using Dynamic Programming. The idea is to simply store the
results of sub problems, so that we do not have to re-compute them when needed later.
This simple optimization reduces time complexities from exponential to polynomial.
 */
public class _0_Fibonacci {
    static int ans[];
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        System.out.println(bup(n));
        ans=new int[n+1];
        System.out.println(topDown(n));
        System.out.println(naive(n));
    }
    static int naive(int n)
    {
        if (n<=1)
            return n;
        else
            return naive(n-1)+naive(n-2);
    }
    static int bup(int n)//no recursion
    {
        int a=0;
        int b=1;
        for (int i=2;i<=n;i++)
        {
            int c=a+b;
            a=b;
            b=c;
        }
        return b;
    }
    static int topDown(int n)// there is recursion
    {
        if (n<=1)
            return n;
        else
        {
            if (ans[n]!=0)
                return ans[n];
            else
            {
                ans[n]=topDown(n-1)+topDown(n-2);
                return ans[n];
            }
        }
    }
}
