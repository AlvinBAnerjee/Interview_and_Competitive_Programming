package _8_Hashing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
Given an array of size n, find all elements in array that appear more than n/k times.
For example, if the input arrays is {3, 1, 2, 2, 1, 2, 3, 3} and k is 4, then the output should be [2, 3].
Note that size of array is 8 (or n = 8), so we need to find all elements that appear more than 2 (or 8/4) times.
There are two elements that appear more than two times, 2 and 3.

There are 2 efficient Methods to Solve this problem
1. Hashing TC: O(n) Space: O(n)
2. Using majority element method TC:O(nk) Space : O(k) works really well if k is small


 */
public class _6_n_by_k_occurences {
    public static void main(String[] args) {
        int arr[]={1,1,1,1,1,1,1,2,2,2,2,3,3,3,4,5,5,5,5,3,2,45,3,4,7};
        int k=7;
        System.out.println(Arrays.toString(method1(arr,k)));
        method2(arr,k);

    }
    public static int[] method1(int arr[],int k)
    {
        ArrayList<Integer> ans=new ArrayList<>();
        HashMap<Integer,Integer> map =new HashMap<>();
        for (int i=0;i<arr.length;i++)
        {
            if (!map.containsKey(arr[i]))
                map.put(arr[i],0);
            map.put(arr[i],map.get(arr[i])+1);
        }
        for (int key: map.keySet()) {
            if (map.get(key)>(arr.length/k))
                ans.add(key);
        }
        int size=ans.size();
        int ans_Array[]=new int[size];
        for (int i=0;i<size;i++)
            ans_Array[i]=ans.get(i);
        return ans_Array;
    }
    public static void method2(int arr[],int k)
    {
        if (k==1)
            return;
        /*
        One thing to remember is that for a given value of k there will be at most k-1 elements with frequency greater than n/k
        Refer elements occurring more than N/3 for more clarity
         */
        int n=arr.length;
        int elements[]=new int[k-1];
        int counts[]=new int[k-1];
        for (int i=0;i<k-1;i++)
        {
            elements[i]=Integer.MAX_VALUE;
            counts[i]=0;
        }
        for(int i=0;i<n;i++)
        {
            int check=0;
            for (int j=0;j<k-1;j++)
            {
                if (elements[j]==arr[i])
                {
                    counts[j]++;
                    check=1;
                    break;
                }
            }
            if (check==1)
                continue;
            for (int j=0;j<k-1;j++)
            {
                if (counts[j]==0)
                {
                    counts[j]++;
                    elements[j]=arr[i];
                    check=1;
                    break;
                }
            }
            if (check==1)
                continue;
            for (int j=0;j<k-1;j++)
            {
               counts[j]--;
            }
        }

        for (int i=0;i<k-1;i++)
        {
            counts[i]=0;
        }

        for (int i=0;i<n;i++)
        {
            for (int j=0;j<k-1;j++)
            {
                if (arr[i]==elements[j])
                    counts[j]++;
            }
        }

        for (int i=0;i<k-1;i++)
        {
            if (counts[i]>(n/k))
                System.out.println(elements[i]);
        }
    }

    static int appearsNBy3(int arr[], int n)//Only for reference
    {
        int count1 = 0, count2 = 0;

        // take the integers as the maximum
        // value of integer hoping the integer
        // would not be present in the array
        int first =  Integer.MIN_VALUE;;
        int second = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {

            // if this element is previously
            // seen, increment count1.
            if (first == arr[i])
                count1++;

                // if this element is previously
                // seen, increment count2.
            else if (second == arr[i])
                count2++;

            else if (count1 == 0) {
                count1++;
                first = arr[i];
            }

            else if (count2 == 0) {
                count2++;
                second = arr[i];
            }

            // if current element is different
            // from both the previously seen
            // variables, decrement both the
            // counts.
            else {
                count1--;
                count2--;
            }
        }

        count1 = 0;
        count2 = 0;

        // Again traverse the array and
        // find the actual counts.
        for (int i = 0; i < n; i++) {
            if (arr[i] == first)
                count1++;

            else if (arr[i] == second)
                count2++;
        }

        if (count1 > n / 3)
            return first;

        if (count2 > n / 3)
            return second;

        return -1;
    }

    // Driver code

}
