package X_CompanyQuestions.Oracle;

import java.util.Arrays;
import java.util.Scanner;

public class StringXor {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        int freq[]=new int[26];
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            freq[ch-'a']++;
        }
        str="";
        for(int i=0;i<26;i++)
        {
            if(freq[i]%2==1)
                str=str+(char)('a'+i);
        }
        int counter=99;
        int mapping[]=new int[26];
        Arrays.fill(mapping, -1);
        for(int i=0;i<str.length();i=i+2)
        {

            if(i==str.length()-1)
            {
                mapping[str.charAt(i)-'a']=1;
            }
            else
            {
                mapping[str.charAt(i)-'a']=counter--;
                mapping[str.charAt(i+1)-'a']=counter--;
            }
        }
        counter=2;
        for(int i=0;i<26;i++)
        {
            if(mapping[i]==-1)
                mapping[i]=counter++;
        }
        System.out.println(Arrays.toString(mapping));

        int xor=0;
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            xor=xor^(mapping[ch-'a']);
        }
        System.out.println("Xor="+xor);
    }
}
