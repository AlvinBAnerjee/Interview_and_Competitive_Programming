package _7_Matrix;
import java.util.*;

public class _1_Transpose {
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
        transpose(arr);
    }
    static void transpose(int arr[][])
    {

        display(arr);
        int n=arr.length;
        for (int i=0;i<n;i++)
        {
            for (int j=i+1;j<n;j++)
            {
                swap(arr,i,j);
            }
        }
        display(arr);


    }
    static void swap(int arr[][],int i,int j)
    {
        int temp=arr[i][j];
        arr[i][j]=arr[j][i];
        arr[j][i]=temp;
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
