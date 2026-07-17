package _2_Recursion;

import java.util.Scanner;

public class Permutation_of_String {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        permute(str,0,str.length()-1);
    }
    static void permute(String str,int l,int r)
    {
        if (r<=l) {
            System.out.println(str);
            return;
        }
        for (int i=l;i<=r;i++)
        {
            String temp=swap(str,l,i);
            permute(temp,l+1,r);
        }
    }
    static String swap(String str,int i,int j)
    {
        char S[]=str.toCharArray();
        char t=S[i];
        S[i]=S[j];
        S[j]=t;
        return String.valueOf(S);
    }

}
