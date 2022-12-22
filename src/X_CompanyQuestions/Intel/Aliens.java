package X_CompanyQuestions.Intel;

import java.util.HashSet;

public class Aliens {
    public static void main(String[] args) {
        String str="1234Hi!Hello678Everyone@GoodByeeto#all";
        int count=0;
        HashSet<Character> numbers=new HashSet<>();
        for(int i=0;i<10;i++)
        {
            numbers.add((char)('0'+i));
        }

        long temp=0;
        long sum=0;
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if(numbers.contains(ch))
            {
                temp=temp*10+(ch-'0');
                count++;
            }
            else
            {
                sum+=temp;
                temp=0;
            }
        }
        sum+=temp;
        char ans[]=new char[str.length()-count];
        int index=0;
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if(numbers.contains(ch))continue;
            ans[index++]=ch;

        }
        System.out.println(new String(ans) + sum);
    }
}
