package _10_Stack;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

/*
Find the largest rectangular area possible in a given histogram where the largest rectangle can be made of
a number of contiguous bars. For simplicity, assume that all bars have same width and the width is 1 unit.
For example, consider the following histogram with 7 bars of heights {6, 2, 5, 4, 5, 1, 6}.
The largest possible rectangle possible is 12
 */
public class _5_Largest_Area_Histogram
{
        public static void main(String[] args) {
        int arr[]={1, 5, 4, 3};
        System.out.println(find_area(arr));
    }
        static int find_area(int arr[])
        {
            int next_smaller[]=find_next_smaller(arr);
            int prev_smaller[]=find_prev_smaller(arr);
    //        System.out.println(Arrays.toString(arr));
    //        System.out.println(Arrays.toString(prev_smaller));
    //        System.out.println(Arrays.toString(next_smaller));

            int area=0;
            int max_area=0;
            for(int i=0;i<arr.length;i++) {
                area=next_smaller[i]-prev_smaller[i]-2;

                int min=find_min(prev_smaller[i],next_smaller[i],arr,i);
                if (min==arr[i])
                    area=area*arr[i];
                else
                    area=0;
                max_area=Math.max(area,max_area);
            }
            return max_area;
        }
        static int find_min(int l,int r,int arr[],int i)
        {

            return Math.min(arr[i],Math.min(arr[l+1],arr[r-1]));
        }
            static int [] reverse(int a[]) {
                int n=a.length;
                int[] b = new int[n];
                int j = n;
                for (int i = 0; i < n; i++) {
                    b[j - 1] = a[i];
                    j = j - 1;
                }
                return b;
            }
        static int[] find_next_smaller(int arr[])//this time we find out the indices
        {
            Stack<Integer> stack=new Stack<>();
            int temp[]=new int[arr.length];
            stack.push(0);
            for (int i=1;i<arr.length;i++)
            {
                int current=arr[i];
                while (!stack.isEmpty()&& current<arr[stack.peek()])
                {
                    temp[stack.pop()]=i;
                }
                stack.push(i);
            }
            while (!stack.isEmpty())
            {
                temp[stack.pop()]=arr.length;
            }
            return (temp);
        }
        static int[] find_prev_smaller(int arr[])//this time we find out the indices
        {
            arr=reverse(arr);
            Stack<Integer> stack=new Stack<>();
            int temp[]=new int[arr.length];
            stack.push(0);
            for (int i=1;i<arr.length;i++)
            {
                int current=arr[i];
                while (!stack.isEmpty()&& current<arr[stack.peek()])
                {
                    temp[stack.pop()]=arr.length-1-i;
                }
                stack.push(i);
            }
            while (!stack.isEmpty())
            {
                temp[stack.pop()]=-1;
            }
            return reverse(temp);
        }
}
