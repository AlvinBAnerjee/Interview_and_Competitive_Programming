package _3_Arrays;
/*
Simply apply Kadanes from both sides
 */
public class _8Maximum_Product_Subarray {
    public static void main(String[] args) {
        int arr[]={2,3,-2,4};
        System.out.println(find(arr));
    }
    static int find(int arr[])
    {
        int n=arr.length;
        int max_so_far=Integer.MIN_VALUE;
        int max=1;
        for (int i=0;i<n;i++)
        {
            max=max*arr[i];
            if (max>max_so_far)
            {
                max_so_far=max;
            }
            if (max==0)
                max=1;
        }

         max=1;
        for (int i=n-1;i>=0;i--)
        {
            max=max*arr[i];
            if (max>max_so_far)
            {
                max_so_far=max;
            }
            if (max==0)
                max=1;
        }

       return max_so_far;
    }
}
