package _3_Arrays;

import java.util.Arrays;
import java.util.Scanner;

public class _4_RotateLeft {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the array");
        String arr[]=sc.nextLine().split(" ");
        System.out.println("Enter how many positions to shift left");
        int d=sc.nextInt();
        d=d% arr.length;
        System.out.println("Array before rotation:"+ Arrays.toString(arr));
        rotate(arr,0, arr.length-1,d);
        System.out.println("Array after rotation:"+Arrays.toString(arr));
    }
    static void rotate(String arr[],int left,int right,int d)
    {
        reverse(arr,0,d-1);
        reverse(arr,0,arr.length-1);
        reverse(arr,0, arr.length-1-d);
    }
    static void reverse(String arr[],int left,int right)
    {
        while (left<right)
        {
            String temp=arr[left];
            arr[left]=arr[right];
            arr[right]=temp;
            left++;
            right--;
        }
    }
}
