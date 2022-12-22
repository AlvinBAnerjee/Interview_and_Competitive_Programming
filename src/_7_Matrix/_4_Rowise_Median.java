package _7_Matrix;

import java.util.Arrays;

/*
Find the median of a rowise sorted array
ex

Input : 1 3 5
        2 6 9
        3 6 9
Output : Median is 5

this is a tricky problem, better refer gfg video but basically the algorithm is using binary search and
Time complexity is O(log(max-min)*log(C)*R)
 */
public class _4_Rowise_Median {
    public static void main(String[] args) {
        int arr[][]={
                {2},
                {1},
                {4},
                {1},
                {2},
                {2},
                {5}

        };
        System.out.println(findMedian(arr,arr.length,arr[0].length));
        System.out.println(" ans"+findMedianTest(arr));

    }
    static int findMedian(int arr[][],int rows,int columns)
    {
        //First we will try to find the maximum and minimum element in the array
        int min=Integer.MAX_VALUE;
        int max=Integer.MIN_VALUE;
        for (int i=0;i<rows;i++)
        {
            min=Math.min(arr[i][0],min);
            max=Math.max(arr[i][columns-1],max);
        }

        //Now in our problem the no of elements are off so the median element will be the
        //middle element. The position of that element when linearly organised will be
        // (rows*column +1)/2 -> this basically is the total no of elements <= the actual median
        int midPos=(rows*columns+1)/2;
        while (min<max)
        {
            int mid=(min+max)/2;
            int elements_less_than_equal_to_mid=calculateTotal(arr,rows,columns,mid);
            if (elements_less_than_equal_to_mid<midPos)
                min=mid+1;
            else
                max=mid;
        }
        return min;
    }
    static int calculateTotal(int arr[][],int rows,int columns,int mid)
    {
        int total=0;
        for (int i=0;i<rows;i++)
        {
            int x= Arrays.binarySearch(arr[i],mid)+1;
            total=total+Math.abs(x);
        }
        return total;
    }

    static  int findMedianTest(int arr[][])
    {
        int row=arr.length;
        int column=arr[0].length;
        int l=Integer.MAX_VALUE;
        int r=Integer.MIN_VALUE;
        for (int i=0;i<row;i++)
        {
            l=Math.min(arr[i][0],l);
            r=Math.max(arr[i][column-1],r);
        }
        int medianPos=(row*column+1)/2;
        int candidate=Integer.MAX_VALUE;
        while (l<=r)
        {
            int mid=(l+r)/2;
            //System.out.println(mid);
            int elementsLessThanEqualToMid=calculateTotal(arr,row,column,mid);
            if (elementsLessThanEqualToMid==medianPos)
            {
                r=mid-1;
                candidate=Math.min(candidate,mid);
            }
            else if (elementsLessThanEqualToMid > medianPos) {
                r = mid - 1;
            }
            else {//elementsLessThanEqualToMid<medianPos
                l=mid+1;
            }
        }
        return candidate;
    }

}
