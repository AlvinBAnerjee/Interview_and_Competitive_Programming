package _2_Recursion;

import java.util.Scanner;
/*
Generate all subsequences of a String
 */
public class GenerateSubsets {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        generate(str,"",0);
    }
    static void generate(String str,String result,int index)
    {
        if (index==str.length()) {
            System.out.println(result);
            return;
        }
        generate(str,result+str.charAt(index),index+1);
        generate(str,result,index+1);
    }
}
