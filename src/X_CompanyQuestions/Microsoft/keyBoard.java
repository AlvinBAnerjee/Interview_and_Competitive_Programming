package X_CompanyQuestions.Microsoft;

import java.util.Scanner;
import java.util.Stack;

public class keyBoard {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        Stack<Character> stack=new Stack<>();
        int cap=0;
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if(ch=='C')
            {
                cap=1-cap;
            }
            else if(ch=='B')
            {
                stack.pop();
            }
            else
            {
                if(cap==1)
                    stack.push(Character.toUpperCase(ch));
                else
                    stack.push(Character.toLowerCase(ch));
            }
        }
        System.out.println(stack.toString());
    }
}
