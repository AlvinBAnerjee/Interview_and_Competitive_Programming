package _2_Recursion;

import java.util.Scanner;

public class TowerOfHanoi {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the no of disks to move from A to C");
        int n=sc.nextInt();
        move(n,'A','B','C');

    }
    static void move(int n,char A,char B,char C)
    {
        if(n==1)
        {
            System.out.println("move 1 from "+A+" to "+C);
        }
        else
        {
            move(n-1,A,C,B);
            System.out.println("move "+n+" from "+A+" to "+C);
            move(n-1,B,A,C);
        }
    }
}
