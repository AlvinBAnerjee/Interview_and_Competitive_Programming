package _7_Matrix;

import java.util.Scanner;

/*
to rotate 90 deg we do:
1. Transpose
2. Reverse it horizontally

O(n^2) where n is the no of rows, and Constant space
 */
public class _2_Rotate_90_degree {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the size of the square matrix");
        int n=sc.nextInt();
        int arr[][]=new int[n][n];
        for (int i=0;i<n;i++)
        {
            for (int j=0;j<n;j++)
            {
                arr[i][j]=sc.nextInt();
            }
        }
        display(arr);
        rotate(arr);
        display(arr);
    }
    static void rotate(int arr[][])
    {
        transpose(arr);
        reverse(arr);
    }
    static void reverse(int arr[][])
    {
        int low=0;
        int high=arr.length-1;
        while (low<high)
        {
            for (int i=0;i<arr.length;i++)

            {
                swap2(arr,i,low,i,high);
            }
            low++;
            high--;
        }
    }
    static void transpose(int arr[][])
    {
        int n=arr.length;
        for (int i=0;i<n;i++)
        {
            for (int j=i+1;j<n;j++)
            {
                swap(arr,i,j);
            }
        }
    }
    static void swap(int arr[][],int i,int j)
    {
        int temp=arr[i][j];
        arr[i][j]=arr[j][i];
        arr[j][i]=temp;
    }
    static void swap2(int arr[][],int a,int b,int c,int d)
    {
        int temp=arr[a][b];
        arr[a][b]=arr[c][d];
        arr[c][d]=temp;
    }
    static void display(int arr[][])
    {
        System.out.println("**********************************");
        for (int i=0;i<arr.length;i++)
        {
            for (int j=0;j<arr.length;j++)
                System.out.print(arr[i][j]+" ");
            System.out.println();
        }
    }
}
