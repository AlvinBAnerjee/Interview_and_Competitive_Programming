package X_CompanyQuestions.Microsoft;

import java.util.ArrayList;
import java.util.Arrays;

public class PalindromeDegree {
    public static ArrayList<Integer> seive(int n)
    {
        boolean notPrime[]=new boolean[n];
        for(int i=2;i<n;i++)
        {
            if(notPrime[i]==true)continue;
            for(int j=2*i;j<n;j=j+i)
            {
                notPrime[j]=true;
            }
        }
        ArrayList<Integer> primes=new ArrayList<>();

        for(int i=1;i<n;i++)
        {
            if(!notPrime[i])
                primes.add(i);
        }
        return primes;
    }
    public static void main(String[] args) {
        String str="microsoft";
        ArrayList<Integer> primes=seive(10004);
        int n=str.length();
        for(int size: primes)
        {
            if(n%size!=0)continue;

            if(isPalindrome(str, size))
            {
                System.out.println(size);
                return;
            }
        }
        System.out.println(-1);
    }
    static  boolean isPalindrome(String str, int size) {
        int l=0;
        int r=str.length()-size;
        while(l<r)
        {
            for(int i=0;i<size;i++)
            {
                if(str.charAt(l+i)!=str.charAt(r+i))
                    return false;
            }
            l=l+size;
            r=r-size;
        }
        return true;

    }
}
