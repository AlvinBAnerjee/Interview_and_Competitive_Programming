package X_CompanyQuestions.flipkart;

import java.util.*;

public class BODMAS {
    static public int evalRPN(String[] tokens) {
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
            else
            {
                stack.push(Integer.parseInt(str));
            }
        }
        return stack.pop();
    }
    static int getPreference(char ch)
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
    public  static List<String> cal(String s){
        List<String> pf = new ArrayList<>();
        boolean flag = false;
        Stack<Character> st = new Stack<>();
        if(true);
        System.out.print("");
        for(int i=0;i<s.length();i++){
            System.out.print("");
            char w = s.charAt(i);
            System.out.print("");
            if(w==' '){
                if(true);
                continue;
            }
            if(w=='('){
                st.push(w);
                if(true);
                flag = false;
                System.out.print("");

            }else if(w==')'){
                if(true);
                flag = false;
                System.out.print("");

                while(!st.isEmpty()){
                    if(st.peek()=='('){
                        st.pop();
                        break;
                    }else{
                        System.out.print("");

                        System.out.print("");

                        pf.add(st.pop()+"");
                    }
                    System.out.print("");

                }
            }else if(w=='+' || w=='-' || w=='*' || w=='/'){
                System.out.print("");

                flag = false;
                if(st.isEmpty()){
                    System.out.print("");

                    st.push(w);
                    System.out.print("");

                }
                else{
                    System.out.print("");

                    while(!st.isEmpty() && getPreference(st.peek())>=getPreference(w)){
                        pf.add(st.pop()+"");
                        System.out.print("");

                    }
                    System.out.print("");

                    st.push(w);
                }
            }else{
                if(flag){
                    System.out.print("");

                    String lastNumber = pf.get(pf.size()-1);
                    System.out.print("");

                    lastNumber+=w;
                    System.out.print("");

                    pf.set(pf.size()-1, lastNumber);
                    System.out.print("");

                }else

                    pf.add(w+"");
                flag = true;
            }
        }
        while(!st.isEmpty()){
            pf.add(st.pop()+"");
        }
        return pf;
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String exp=sc.nextLine();
        List<String> temp= cal(exp);
        String postfix[]=new String[temp.size()];
        for(int i=0;i<postfix.length;i++)
        {
            postfix[i]=temp.get(i);
        }

        int correctAns=evalRPN(postfix);
       // System.out.println("correct ans="+correctAns);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        ArrayList<String> expression=new ArrayList<>();
        int start=0;

        for(int i=0;i<exp.length();i++)
        {
            char ch=exp.charAt(i);
            if(ch=='*' || ch=='+' || ch=='-')
            {
                expression.add(exp.substring(start, i));
                expression.add(""+ch);
                start=i+1;
            }
        }
        expression.add(exp.substring(start));
        //System.out.println(expression.toString());
        HashSet<Integer> partial=rec(0, expression.size()-1, expression);
        //System.out.println("partial="+partial.toString());
        int score=0;
        for(int x: arr)
        {
            if(x==correctAns)
            {
                score+=5;
                //System.out.println(5);
            }
            else if(partial.contains(x))
            {
                score+=2;
                //System.out.println(2);
            }
            else {
                //System.out.println(0);
            }

        }
        System.out.println(score);



    }
    public static  HashSet<Integer> rec(int i, int j, ArrayList<String> exp)
    {

        if(i==j)
        {
            HashSet<Integer> ans=new HashSet<>();
            ans.add(Integer.parseInt(exp.get(i).trim()));
            return ans;
        }
        HashSet<Integer> ans=new HashSet<>();
        for(int k=i+1;k<=j-1;k=k+2)
        {
            char ch=exp.get(k).charAt(0);
            HashSet<Integer> left=rec(i,k-1,exp);
            HashSet<Integer> right=rec(k+1, j, exp);
            if(ch=='*')
            {
                for(int x: left)
                {
                    for(int y: right)
                    {
                        ans.add(x*y);
                    }
                }
            }
            else if(ch=='-')
            {
                for(int x: left)
                {
                    for(int y: right)
                    {
                        ans.add(x-y);
                    }
                }
            }
            else
            {
                for(int x: left)
                {
                    for(int y: right)
                    {
                        ans.add(x+y);
                    }
                }
            }
        }
        return ans;
    }

}
/*
5*4-3*2
4
-10 10 14 90
 */