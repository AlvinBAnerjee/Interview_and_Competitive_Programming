package _7_Matrix;

import java.util.Scanner;

public class _3_Search_in_Rowise_and_Columnwise_Sorted_Matrix {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int arr[][]={{1,3,5,7},
                     {10,11,16,20},
                     {23,30,34,60 }};
        int key=34;
        findKey(arr,key,3,4);


    }
    static void findKey(int arr[][],int key,int row,int column)
    {
        int i=0;
        int j=column-1;
        while (i<=row-1&&j>=0)
        {
            if (arr[i][j]==key)
                break;
            else if(key>arr[i][j])
                i++;
            else
                j--;
        }
        if ((i>=0&&i<=row-1)&&(j>=0&&j<=column-1))
            System.out.println("Found at: ("+i+","+j+")");
        else
            System.out.println("Not found");
    }
}
