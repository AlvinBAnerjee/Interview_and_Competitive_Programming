package _6_Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class _2_Intersection_of_2_Sorted_Arrays {
    public static void main(String[] args) {
        int arr1[] = {1, 3, 4, 5, 7};
        int arr2[] = {2, 3, 5, 6};
        System.out.println(Arrays.toString(getIntersection(arr1,arr2)));
    }

    public static int[] getIntersection(int arr1[], int arr2[]) {
        HashSet<Integer> inter = new HashSet<>();
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        int i = 0;
        int j = 0;
        while (i < arr1.length && j < arr2.length) {
            if (arr1[i] == arr2[j]) {
                inter.add(arr1[i]);
                i++;
                j++;
            } else if (arr1[i] < arr2[j]) {
                i++;

            } else {
                j++;
            }
        }


        int arr[] = new int[inter.size()];
        i = 0;
        for (int item : inter) {
            arr[i++] = item;
        }
        Arrays.sort(arr);
        return arr;
    }
}
