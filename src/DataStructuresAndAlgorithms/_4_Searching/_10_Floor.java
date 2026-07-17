package _4_Searching;

public class _10_Floor {
    public static void main(String[] args) {
        long arr[]={1,2,7,11,7,6};
        System.out.println(FindFloor_(arr,2));
    }
    static long FindFloor_(long arr[],long key)
    {
        if(arr[0]>key)
            return -1;
        int l=0;
        int r=arr.length-1;
        long candidate=-1;
        while (l<=r)
        {
            int mid=(l+r)/2;
            if (arr[mid]==key)
                return arr[mid];
            else if (arr[mid]<key)
            {
                candidate=Math.max(candidate,arr[mid]);
                l=mid+1;

            }
            else // mid is larger than key
            {
                r=mid-1;
            }

        }
        return candidate;
    }
}
