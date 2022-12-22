package X_CompanyQuestions.MathWorks;

import java.util.Scanner;

public class ReduceFraction {
    static int gcd(int a,int b)
    {

        if (a<b)
            return gcd(b,a);
        if (b==0)
            return a;
        else
            return gcd(b,a%b);
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int a=sc.nextInt();
        int b=sc.nextInt();
        int c=sc.nextInt();
        int d=sc.nextInt();
        int n=a*d+c*b;
        int den=b*d;
        int sign=1;
        if(n*d<0)
        {
            sign=-1;
        }
        int div=gcd(Math.abs(n),Math.abs(den));
        System.out.println(sign+" "+(n/div)+"/"+(den/div));
    }
}
