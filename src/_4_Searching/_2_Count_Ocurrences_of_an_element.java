package _4_Searching;

public class _2_Count_Ocurrences_of_an_element {
    public static void main(String[] args) {
        int n=10;
        int arr[]={1,2,3,4,5,5,5,5,5,6};
        System.out.println(find_occurence(arr,n,5));
    }
    static int find_occurence(int arr[],int n,int find)
    {
        int left=find_l_occurence(arr,n,find);
        int right=find_r_occurence(arr,n,find);
        if (left!=-1 && right!=-1)
        {
            return right-left+1;
        }
        return -1;
    }
    static int find_l_occurence(int arr[],int n, int find)
    {
        int l=0;int r=arr.length-1;

        while (l<=r)
        {
            int mid=(l+r)/2;
            if (arr[mid]==find)
            {
                if (mid>0&&arr[mid-1]==arr[mid])
                {
                    r=mid-1;
                }
                else
                {
                    return mid;

                }
            }
            else if (arr[mid]>find)
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
    static int find_r_occurence(int arr[],int n, int find)
    {
        int l=0;int r=arr.length-1;
        while (l<=r)
        {
            int mid=(l+r)/2;
            if (arr[mid]==find)
            {
                if (mid!=n-1&&arr[mid+1]==arr[mid])
                {
                    l=mid+1;
                }
                else
                {
                    return mid;

                }
            }
            else if (arr[mid]>find)
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
