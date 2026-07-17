package _1_BitMagic;

import java.util.Scanner;

public class two_odd_occurences {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        int xor=0;
        for (int i=0;i<n;i++) {
            arr[i]=sc.nextInt();
            xor = xor ^ arr[i];
        }
        System.out.println(xor);
        xor=xor&~(xor-1);//right most bit
        System.out.println(xor);
        int num1=0;
        int num2=0;
        for (int i=0;i<n;i++)
        {
            if ((arr[i]&xor)!=0)
                num1=num1^arr[i];
            else
                num2=num2^arr[i];
        }
        System.out.println("The numbers are "+num1+" and "+num2);
    }
}
