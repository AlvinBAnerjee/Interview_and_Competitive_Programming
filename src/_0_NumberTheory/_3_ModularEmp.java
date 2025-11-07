package _0_NumberTheory;

public class _3_ModularEmp {
    public static void main(String[] args) {
        int s=2;
        int n=3;
        int m=4;
        long x=FastExp(s,n,10);
        System.out.println(FastExp(x,m,10000007));
    }
    static long FastExp(long A, long B, long C)
    {
        long res=1;

        for(int i=0;i<64;i++)
        {
            if(((B>>i)&1)==1)
            {
                long temp=pow2(A, 1<<i,C )%C;
                res=res*temp;
                res=res%C;
            }
        }


        return res%C;
    }
    static long pow2(long A, long B, long C)
    {
        long start=1;
        long x=A%C;
        while(start!=B)
        {
            x=x*x;
            x=x%C;
            start=start*2;
        }
        return  x%C;
    }
    public int powMod(int x, int n, int M) {
        // code here
        x=x%M;
        if(x==1)return 1;
        if(n==0)return 1;

        if(n==1) return x;

        if(n%2==1)
        {
            long temp =  ((long)x*(long)powMod(x,n-1,M));
            temp=temp%M;
            return (int)temp;
        }
        else
        {
            long temp =  ((long)powMod(x,n/2,M))%M;
            temp=temp*temp;
            temp=temp%M;
            return (int)temp;
        }
    }
}
