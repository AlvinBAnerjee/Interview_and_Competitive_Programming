package _0_NumberTheory;

import java.util.Scanner;

public class _1_Eucleidian_Algorithm {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter two numbers to find its GCD");
        System.out.println(gcd(sc.nextInt(),sc.nextInt()));
    }
    static int gcd(int a,int b)
    {

        if (a<b)
            return gcd(b,a);
        if (b==0)
            return a;
        else
            return gcd(b,a%b);
    }
}
