package _1_BitMagic;

import java.util.Scanner;

public class Power_of_Two {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        if (isPowerofTwo(n))
            System.out.println("Yes is a power of two");
        else
            System.out.println("No not power of two");
    }
    static boolean isPowerofTwo(int n){
        if (n==0)
            return false;
        if ((n&(n-1))==0)
            return true;
        return false;
    }
}
