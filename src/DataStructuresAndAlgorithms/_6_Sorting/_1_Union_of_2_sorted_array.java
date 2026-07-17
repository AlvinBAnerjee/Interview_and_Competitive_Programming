package _6_Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
Both the arrays are sorted

Input : arr1[] = {1, 3, 4, 5, 7}
        arr2[] = {2, 3, 5, 6}
         Intersection : {3, 5}


 */

public class _1_Union_of_2_sorted_array {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int arr1[] = {1, 3, 4, 5, 7};
        int arr2[] = {2, 3, 5, 6};
        System.out.println(Arrays.toString(getUnion(arr1,arr2)));

    }
    public static int [] getUnion(int arr1[], int arr2[])
    {
        ArrayList<Integer> inter=new ArrayList<>();
        int i=0;
        int j=0;
        while (i<arr1.length&&j<arr2.length){
            if (arr1[i]<arr2[j])
            {
                int min=arr1[i];
                while (i<arr1.length&&arr1[i]==min)
                {
                    i++;
                }

                inter.add(min);
            }
            else
            {
                int min=arr2[j];
                while (j<arr2.length&&arr2[j]==min)
                {
                    j++;
                }
                while (i<arr1.length&&arr1[i]==min)
                {
                    i++;
                }
                inter.add(min);
            }
        }


        while (i<arr1.length)
            inter.add(arr1[i++]);

        while (j<arr2.length)
            inter.add(arr2[j++]);


        int arr[]=new int[inter.size()];

        for (int k = 0; k < inter.size(); k++) {
            arr[k] = inter.get(k);
        }
        return arr;
    }
}
