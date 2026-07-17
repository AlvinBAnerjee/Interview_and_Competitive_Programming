package _10_Stack;

import java.util.Arrays;
import java.util.Stack;
/*
Given an array, print the Next Greater Element (NGE) for every element. The Next greater Element for an element x is the first greater element on the right side of x in the array. Elements for which no greater element exist, consider the next greater element as -1.

Examples:

For an array, the rightmost element always has the next greater element as -1.
For an array that is sorted in decreasing order, all elements have the next greater element as -1.
For the input array [4, 5, 2, 25], the next greater elements for each element are as follows.
Element       NGE
   4      -->   5
   5      -->   25
   2      -->   25
   25     -->   -1
 */
public class _4_Next_Greater_Element {
    public static void main(String[] args) {
        int arr[]={5,15,10,8,6,12,9,18};
        System.out.println(Arrays.toString(find_next_greater(arr)));

    }
    static int[] find_next_greater(int arr[])
    {
        Stack<Integer> stack=new Stack<>();
        int temp[]=new int[arr.length];
        stack.push(0);
        for (int i=1;i<arr.length;i++)
        {
            int current=arr[i];
            while (!stack.isEmpty()&& current>arr[stack.peek()])
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
