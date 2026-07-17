package _10_Stack;

import java.util.Arrays;
import java.util.Stack;

public class _4_Next_Smaller_Element {
    public static void main(String[] args) {
        int arr[]={5,15,10,8,6,12,9,18};
        System.out.println(Arrays.toString(find_next_smaller(arr)));
    }
    static int[] find_next_smaller(int arr[])
    {
        Stack<Integer> stack=new Stack<>();
        int temp[]=new int[arr.length];
        stack.push(0);
        for (int i=1;i<arr.length;i++)
        {
            int current=arr[i];
            while (!stack.isEmpty()&& current<arr[stack.peek()])
            {
                temp[stack.pop()]=current;
            }
            stack.push(i);
        }
        while (!stack.isEmpty())
        {
            temp[stack.pop()]=-1;
        }
        return temp;
    }

}
