package _18_DP;

public class __4_Count_of_SubSetSum {
    /*
    just did the recursive formula the dp can be done in a similar way
     */
    public static void main(String[] args) {
        __4_Count_of_SubSetSum ob=new __4_Count_of_SubSetSum();
        int arr[]={1,2,3,3};
        int x=6;
        System.out.println(ob.findCountRecursive(arr,x,arr.length));
    }

    int findCountRecursive(int arr[],int sum,int item)
    {
        if (sum==0)
            return 1;
        if (item==0)
            return 0;
        if (sum>=arr[item-1])
        {
            return findCountRecursive(arr,sum-arr[item-1],item-1) + findCountRecursive(arr, sum,item-1);
        }
        return findCountRecursive(arr, sum,item-1);
    }
}
