package X_CompanyQuestions.UI;

import java.util.Arrays;

public class _2_Grouping_Marbles {
    public static void main(String[] args) {
        int n=7;
        int A[]={2,3};
        int B[]={4,6};
        int C[]={1,1,1,1,1,5,7};
        System.out.println(lis(A,B,C));
    }
    static int lis(int A[], int B[], int C[])
    {
        int arr[]=new int[A.length+B.length+C.length];
        int index=0;
        for(int i=0;i<A.length;i++)
        {
            arr[index++]=A[i];
        }
        for(int i=0;i<B.length;i++)
        {
            arr[index++]=B[i];
        }
        for(int i=0;i<C.length;i++)
        {
            arr[index++]=C[i];
        }

        return arr.length-findLISBinarySearch(arr);
    }
    static int ceilIdx(int tail[], int l, int r, int key)
    {
        while (r > l) {
            int m = l + (r - l) / 2;
            if (tail[m] >= key)
                r = m;
            else
                l = m+1;
        }

        return r;
    }

    static int findLISBinarySearch(int arr[])
    {

        int n=arr.length;
        int[] tail = new int[n];
        int len =1;

        tail[0] = arr[0];

        for (int i = 1; i < n; i++) {

            if(arr[i] > tail[len - 1])
            {
                tail[len] = arr[i];
                len++;
            }
            else{
                int c = ceilIdx(tail, 0, len - 1, arr[i]);

                tail[c] = arr[i];
            }
        }

        return len;
    }
}
