package X_CompanyQuestions.Intel;
import java.lang.reflect.Array;
import java.util.*;
public class Postfix {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String expression=sc.nextLine();
        int p=sc.nextInt();
        int m=sc.nextInt();
        String temp[]=infixToPostfix(expression);
        for(int i=0;i<=m;i++)
        {
            if(evalRPN(temp, i)%m==p ) {
                System.out.println(i);
                break;
            }
        }
    }
    static public int evalRPN(String[] tokens, int val) {
        Stack<Integer> stack=new Stack<>();
        for(int i=0;i<tokens.length;i++)
        {

            String str=tokens[i];
            if(str.equals("+"))
            {
                int a=stack.pop();
                int b=stack.pop();
                stack.push(a+b);
            }
            else if(str.equals("-"))
            {
                int a=stack.pop();
                int b=stack.pop();
                stack.push(b-a);
            }else if(str.equals("/"))
            {
                int a=stack.pop();
                int b=stack.pop();
                stack.push(b/a);
            }else if(str.equals("*"))
            {
                int a=stack.pop();
                int b=stack.pop();
                stack.push(b*a);
            }
            else if(str.equals("x"))
            {
                stack.push(val);
            }
            else
            {
                stack.push(Integer.parseInt(str));
            }
        }
        return stack.pop();
    }
    static int Prec(char ch)
    {
        switch (ch) {
            case '+':
                return 1;
            case '-':
                return 2;
            case '*':
                return 3;
        }
        return -1;
    }
    static String[] infixToPostfix(String exp)
    {

        ArrayList<String> result = new ArrayList<>();


        Deque<Character> stack
                = new ArrayDeque<Character>();

        for (int i = 0; i < exp.length(); ++i) {
            char c = exp.charAt(i);


            if (Character.isLetterOrDigit(c))
                result .add(""+ c);


            else if (c == '(')
                stack.push(c);


            else if (c == ')') {
                while (!stack.isEmpty()
                        && stack.peek() != '(') {
                    result.add(""+stack.peek());
                    stack.pop();
                }

                stack.pop();
            }
            else
            {
                while (!stack.isEmpty()
                        && Prec(c) <= Prec(stack.peek())) {

                    result.add(""+stack.peek());
                    stack.pop();
                }
                stack.push(c);
            }
        }


        while (!stack.isEmpty()) {
            if (stack.peek() == '(')
                return null;
            result.add(""+stack.peek());
            stack.pop();
        }
        String res[]=new String[result.size()];
        for(int i=0;i<res.length;i++)
        {
            res[i]=result.get(i);
        }
        return res;
    }
}
