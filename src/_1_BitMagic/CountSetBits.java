package _1_BitMagic;

import java.util.Scanner;

public class CountSetBits {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        System.out.println("Method 1:"+method1(n));
        System.out.println("Method 2:"+method2(n));
    }
    static int method1(int n)//take O(no of bits)
    {
        int count=0;
        for (int i=0;i<Integer.BYTES*8;i++)
        {
            if ((n&1)==1)
                count++;
            n=n>>1;
        }
        return count;
    }
    static int method2(int n)//take O(no of set bits) Brian Kerninghams algo
    {
        int count=0;
        while (n>0)
        {
            n=n&(n-1);
            count++;
        }
        return count;
    }
}
