package _4_Searching;

public class _8_Allocate_Minimum_no_of_Pages {
    public static void main(String[] args) {
        int arr[]={12, 34, 67, 90};
        int k=2;
        _8_Allocate_Minimum_no_of_Pages ob=new _8_Allocate_Minimum_no_of_Pages();
        System.out.println(ob.findMinPage(arr,k));

    }
    public int findMinPage(int arr[],int k)
    {
        int l=0;
        int r=Integer.MAX_VALUE;
        int candidate=Integer.MAX_VALUE;
        while (l<=r)
        {
            int mid=(l+r)/2;
            boolean b=isFeasible(arr,k,mid);
            if (b)
            {
                candidate=Math.min(candidate,mid);
                r=mid-1;
            }
            else
            {
                l=mid+1;
            }
        }
        return candidate;
    }
    public boolean isFeasible(int arr[],int k,int size)
    {
        int count=1;
        int sum=0;
        for (int i=0;i<arr.length;i++)
        {
            if (arr[i] > size)
                return false;
            if (sum+arr[i]>size)
            {
                sum=arr[i];
                count++;
            }
            else
            {
                sum+=arr[i];
            }
        }
        if (count>k)
            return false;
        return true;
    }
}
