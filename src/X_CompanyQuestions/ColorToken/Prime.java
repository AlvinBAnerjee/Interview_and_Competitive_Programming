package X_CompanyQuestions.ColorToken;

import java.util.*;
import java.util.Scanner;

public class Prime {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int t=sc.nextInt();
        int n=sc.nextInt();
        int dec=sc.nextInt();
        int inc=sc.nextInt();

        ArrayList<Integer> three=new ArrayList<>();
        ArrayList<Integer> five=new ArrayList<>();
        ArrayList<Integer> seven=new ArrayList<>();
        for(int i=1;i<20;i++)
        {
            three.add((int)Math.pow(3,i));
        }
        for(int i=1;i<10;i++)
        {
            five.add((int)Math.pow(5,i));
        }
        for(int i=1;i<10;i++)
        {
            seven.add((int)Math.pow(7,i));
        }
        TreeSet<Integer> set=new TreeSet<>();
        for(int x: three)
        {
            for(int y : five)
            {
                set.add(x+y);
            }
        }
        for(int x: five)
        {
            for(int y : seven)
            {
                set.add(x+y);
            }
        }
        for(int x: five)
        {
            for(int y : three)
            {
                set.add(x+y);
            }
        }
        if(set.contains(n)) System.out.println(0);
        else {
            Integer l=set.lower(n);
            Integer r=set.higher(n);
            long res1=Integer.MAX_VALUE;
            long res2=Integer.MAX_VALUE;
            if(l!=null)
            res1=Math.abs(l-n)*dec;
            if(r!=null)
             res2=Math.abs(n-r)*inc;
            System.out.println(Math.min(res1,res2));
        }
    }
}
