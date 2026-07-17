package _4_Searching;
/*
Given an array of integers. Find a peak element in it. An array element is a peak if it is NOT smaller than its neighbours.
For corner elements, we need to consider only one neighbour.

Example:

Input: array[]= {5, 10, 20, 15}
Output: 20
The element 20 has neighbours 10 and 15,
both of them are less than 20.

Input: array[] = {10, 20, 15, 2, 23, 90, 67}
Output: 20 or 90
The element 20 has neighbours 10 and 15,
both of them are less than 20, similarly 90 has neighbous 23 and 67.




I have done my solution. The solution is correct but the code looks messy
Lets look at Geeks for Geeks solution
static int getPeak(int arr[], int n)
	{
		int low = 0, high = n - 1;

		while(low <= high)
		{
			int mid = (low + high) / 2;

			if((mid == 0 || arr[mid - 1] <= arr[mid]) &&
				(mid == n - 1 || arr[mid + 1] <= arr[mid]))
				return mid;
			if(mid > 0 && arr[mid - 1] >= arr[mid])
				high = mid -1;
			else
				low = mid + 1;
		}

		return -1;
	}

 */
public class _4_Find_Peak_Element {
    public static void main(String[] args) {
        int arr[]={8,6,2,3,4,5};
        System.out.println(find_peak_element(arr));
    }
    static  int find_peak_element(int arr[])//returns the index of peak element
    {
        int l=0;
        int r= arr.length-1;
        while (l<=r)
        {
            int mid=(l+r)/2;
            if (mid==l&&mid==r)
                return mid;
            else if (mid==l)
            {
                if (arr[mid]>arr[mid+1])
                    return mid;
                else
                    l++;
            }
            else if(mid==r)
            {
                if (arr[mid]>arr[mid-1])
                    return mid;
                else
                    r--;
            }
            else
            {
                if (arr[mid]>arr[mid-1]&&arr[mid]>arr[mid+1])
                    return mid;
                else if(arr[mid]<arr[mid-1])
                    r=mid-1;
                else
                    l=mid+1;
            }
        }
        return -1;
    }
}
