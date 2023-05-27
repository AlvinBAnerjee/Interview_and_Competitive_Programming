package _1_BitMagic;

public class  x_only_one_number_occurs_once {
    /*
    Given an integer array nums where every element appears three times except for one, which appears exactly once.
    Find the single element and return it.

You must implement a solution with a linear runtime complexity and use only constant extra space.



Example 1:

Input: nums = [2,2,3,2]
Output: 3
Example 2:

Input: nums = [0,1,0,1,0,1,99]
Output: 99

For the solution guide look at PepCoding
     */
    public static void main(String[] args) {
        int arr[]={2,2,2,7,7,7,88,88,88,9};
        System.out.println(solve(arr));
    }
    public static int solve(int arr[])
    {
        int n=arr.length;
        int three_n0=-1;
        int three_n1=0;
        int three_n2=0;
        for (int i=0;i<n;i++)
        {
            int common0=three_n0 & arr[i];
            int common1=three_n1 & arr[i];
            int common2=three_n2 & arr[i];

            three_n0=three_n0 & (~common0);
            three_n1=three_n1 | common0;

            three_n1=three_n1 & (~common1);
            three_n2=three_n2 | common1;

            three_n2=three_n2 & (~common2);
            three_n0=three_n0 | common2;

        }
        return three_n1;

    }
}
