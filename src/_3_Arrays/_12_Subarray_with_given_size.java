package _3_Arrays;

public class _12_Subarray_with_given_size {
    public static void main(String[] args) {
        int arr[]={1, 4};
        int sum=0;
        System.out.println(isPresent_nonNegative(arr,sum));
    }
    static boolean isPresent_nonNegative(int arr[],int value)//Non Negative numbers
    {
        /*
        Efficient Approach: There is an idea if all the elements of the array are positive.
        If a subarray has sum greater than the given sum then there is no possibility that adding elements to the current
         subarray the sum will be x (given sum). Idea is to use a similar approach to a sliding window.
         Start with an empty subarray, add elements to the subarray until the sum is less than x.
         If the sum is greater than x, remove elements from the start of the current subarray.
Algorithm:

Create three variables, l=0, sum = 0
Traverse the array from start to end.
Update the variable sum by adding current element, sum = sum + array[i]
If the sum is greater than the given sum, update the variable sum as sum = sum – array[l], and update l as, l++.
If the sum is equal to given sum, print the subarray and break the loop.
         */
        int sum=0;
        int left=0;
        int right=0;
        while (right>=left &&right<arr.length)
        {
            if (sum>value)
            {
                sum=sum-arr[left];
                left++;
            }
            else
            {
                sum=sum+arr[right];
                right++;
            }
            //System.out.println(sum+" "+value);
            if (sum==value&&left!=right)
                return true;
        }
        return false;
    }
    static boolean isPresent_allCases(int arr[],int value)//
    {
        /*
        will be discussed after hashmap
         */
        return true;
    }
}
