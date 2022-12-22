package _1_BitMagic;

import java.util.Scanner;

public class Kth_bit_is_set_or_not {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter a no and kth bit to check");
        int n=sc.nextInt();
        int k=sc.nextInt();
        if ((n>>(k-1)&1)==1)
            System.out.println("Yes it is set");
        else
            System.out.println("Not set");
    }
}
