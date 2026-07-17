package _6_Sorting;

import java.util.Arrays;

public class _0_Java_Inbuilt_Searching {
    public static void main(String[] args) {
        ;
    }
    private static void check(int[] arr, int toCheckValue)
    {
        // sort given array
        Arrays.sort(arr);

        // check if the specified element
        // is present in the array or not
        // using Binary Search method
        int res = Arrays.binarySearch(arr, toCheckValue);

        boolean test = res > 0 ? true : false;

        // Print the result
        System.out.println("Is " + toCheckValue
                + " present in the array: " + test);
    }
}
