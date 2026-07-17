package _10_Stack;

import java.util.Arrays;
import java.util.Stack;

/*
The stock span problem is a financial problem where we have a series of n daily price quotes for a stock and
we need to calculate span of stock’s price for all n days.
The span Si of the stock’s price on a given day i is defined as the maximum number of consecutive days
just before the given day,
for which the price of the stock on the current day is less than or equal to its price on the given day.
For example, if an array of 7 days prices is given as {100, 80, 60, 70, 60, 75, 85,200},
 then the span values for corresponding 7 days are {1, 1, 1, 2, 1, 4, 6,8}


 */
public class _2_Stock_Span {
    public static void main(String[] args) {
        int arr[]={5,4,3,2,1};
        System.out.println(Arrays.toString(find_span(arr)));
        System.out.println(Arrays.toString(find(arr)));

    }
    static  int [] find_span(int arr[])
    {
        int temp[]=new int[arr.length];
        Stack<Integer> stack=new Stack<>();
        stack.push(0);
        temp[0]=1;
        for(int i=1;i<arr.length;i++)
        {
            int current=arr[i];
            while (!stack.isEmpty()&&current>=arr[stack.peek()])
            {
                stack.pop();
            }
            if (stack.isEmpty())
                temp[i]=i+1;
            else
            {
                temp[i]=i-stack.peek();
            }
            stack.push(i);
        }
        return temp;

    }
    static int [] find(int arr[])
    {
        Stack<Integer> stack=new Stack<>();
        stack.push(0);
        int ans[]=new int[arr.length];
        ans[0]=1;
        for (int i=1;i<arr.length;i++)
        {
            int current=arr[i];
            while (!stack.isEmpty()&&current>=arr[stack.peek()])
            {
                stack.pop();
            }
            if (stack.isEmpty())
                ans[i]=(i+1);
            else
            {
                ans[i]=i-stack.peek();
            }
            stack.push(i);
        }
        while (!stack.isEmpty())
        {
            arr[stack.pop()]=1;
        }
        return ans;
    }
}
