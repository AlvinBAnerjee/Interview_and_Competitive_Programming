package X_CompanyQuestions.Oracle;

import java.util.Scanner;
import java.util.Stack;

public class ModifyStrings {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        int freq[]=new int[26];
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            freq[ch-'a']++;
        }
        Stack<Character> stack=new Stack<>();

        for(int i=str.length()-1;i>=0;i--)
        {
            char ch=str.charAt(i);
            while (!stack.isEmpty() &&stack.peek()>= ch && freq[stack.peek()-'a']>1)
            {
                char x=stack.pop();
                freq[x-'a']--;
            }
            stack.push(ch);
        }
        System.out.println(stack.toString());
    }
}
