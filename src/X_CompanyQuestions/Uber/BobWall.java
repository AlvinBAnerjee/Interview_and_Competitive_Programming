package X_CompanyQuestions.Uber;

import java.util.Scanner;

public class BobWall {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        int k=sc.nextInt();
        System.out.println(solve(str, k));
    }
    static int solve(String str, int k)
    {
        int res=0;
        for(int i=0;i<26;i++)
        {
            res=Math.max(res, f(str,k, (char)('a'+i)));
        }
        return res;
    }
    static  int f(String str, int k, char c)
    {
        int res=0;
        int start=0;
        int count=0;
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if(ch!=c)
                count++;
            while(count>k)
            {
                if(str.charAt(start)!=c)count--;
                start++;
            }
            res=Math.max(res, i-start+1);
        }
        return res;
    }
}
