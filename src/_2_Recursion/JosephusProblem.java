package _2_Recursion;

public class JosephusProblem {
    public int findTheWinner(int n, int k) {
        return rec(n,k) +1;
    }
    public int rec(int n , int k)
    {
        if(n==1)return 0;

        return (rec(n-1,k) + k)%n;
    }
}
