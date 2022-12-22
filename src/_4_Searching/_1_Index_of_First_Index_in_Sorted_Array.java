package _4_Searching;

import java.util.Arrays;
import java.util.Scanner;

public class _1_Index_of_First_Index_in_Sorted_Array {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the size of the arrays");
        int n=sc.nextInt();
        System.out.println("Enter the elements");
        int arr[]=new int[n];
        for (int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        Arrays.sort(arr);
        System.out.println("The sorted array is as follows "+Arrays.toString(arr));
        System.out.println("Enter the element you want  to search");
        int find=sc.nextInt();
        System.out.println(find_l_occurence(arr,find));



    }
    static int find_l_occurence(int arr[], int find)
    {
        int l=0;int r=arr.length-1;

        while (l<=r)
        {
            int mid=(l+r)/2;
            if (arr[mid]==find)
            {
                if (mid>0&&arr[mid-1]==arr[mid])
                {
                    r=mid-1;
                }
                else
                {
                    return mid;

                }
            }
            else if (arr[mid]>find)
            {
                r=mid-1;
            }
            else
            {
                l=mid+1;
            }
        }
        return -1;
    }

}
