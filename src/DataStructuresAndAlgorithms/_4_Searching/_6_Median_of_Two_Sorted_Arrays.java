package _4_Searching;
/*
Super complex to explain just google tushar roys solution or geeks for geeks solution
 */

import java.util.Arrays;

public class _6_Median_of_Two_Sorted_Arrays {
    public static void main(String[] args) {
        int arr2[]={1,3};
        int arr1[]= {2};

        System.out.println(findMedianSortedArrays(arr1,arr2));

    }
    public static double findMedianSortedArrays(int input1[], int input2[]) {
        //if input1 length is greater than switch them so that input1 is smaller than input2.
        if (input1.length > input2.length) {
            return findMedianSortedArrays(input2, input1);
        }
        int n1=input1.length;
        int n2=input2.length;
        int l=0;
        int r=n1;
        while (l<=r)
        {
            int mid=(l+r)/2;


            int cut1=mid;
            int cut2=(n1+n2+1)/2-cut1;

            int l1=(cut1==0)?Integer.MIN_VALUE:input1[cut1-1];
            int l2=(cut2==0)?Integer.MIN_VALUE:input2[cut2-1];

            int r1=(cut1==n1)?Integer.MAX_VALUE:input1[cut1];
            int r2=(cut2==n2)?Integer.MAX_VALUE:input2[cut2];

            System.out.println(l+" "+r+ " "+mid+" "+l1+" "+l2+ " "+r1+ " "+r2 +" "+(l1<=r2)+" "+(l2<=r1) );

            if (l1<=r2 && l2<=r1)
            {
                if ((n1+n2)%2==1)
                {
                    return Integer.max(l1,l2);
                }
                else
                {
                    return (Integer.max(l1,l2)+(double)Integer.min(r1,r2))/2;
                }
            }
            else if(l1>r2)
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



