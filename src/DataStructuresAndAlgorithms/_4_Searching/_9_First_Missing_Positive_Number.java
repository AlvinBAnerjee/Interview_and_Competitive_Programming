package _4_Searching;
/*
Given an unsorted integer array, find the first missing positive integer.

Example:

Given [1,2,0] return 3,

[3,4,-1,1] return 2,

[-8, -7, -6] returns 1

Your algorithm should run in O(n) time and use constant space.

int Solution::firstMissingPositive(vector<int> &A) {
// A[i] should be positioned at A[i]-1 if A[i]>0
// But in case of repeated characters, consider this case: 1 2 3 1 4 6 3
// Here 2 should be on the third position.
// But if we somehow could break this into : 1 2 3 4 missing 6 ...
// i.e have a sequence and separate the repeating characters.
// then we can get answer in just one iteration by checking if A[i]!=i+1 return i+1


// To "convert" our array, what we need to do is simple: Just swap the
// elements with elements at their correct position. But why should it work for repeated
// elements? Simple, because it does.

// What it effectively does is that it would postion repeated elements to
// the missing element position.
int n=A.size();
for(int i=0;i<n;i++)
{
    // If A[i] is already at correct position or have no valid position
    if(A[i]==i+1||A[i]<=0||A[i]>n||A[i]==A[A[i]-1])continue;

    // else swap the element with its correct postion.
    swap(A[i],A[A[i]-1]);
    i--;

}


 */
public class _9_First_Missing_Positive_Number {
    public static void main(String[] args) {
        int a[]={699, 2, 690, 936, 319, 784, 562, 35, 151, 1,-1,3,699,6,4};
        System.out.println(firstMissingPositive(a));
    }
    public static int firstMissingPositive(int[] A) {
        for(int i=0;i<A.length;i++)
        {

            // If A[i] is already at correct position or have no valid position
            if(A[i]==i+1||A[i]<=0||A[i]>A.length||A[i]==A[A[i]-1])continue;
            // else swap the element with its correct postion.
            //swap(A[i],A[A[i]-1]);
            int x=A[i];
            int y=A[A[i]-1];
            A[i]=y;
            A[x-1]=x;
            i--;
            //Why i--. When swapping we are ignoring the the new swapped element in ith position. We will have to re insert it in the correct position

        }
        for(int i=0;i<A.length;i++)
        {
            if(A[i]!=i+1)
                return i+1;
        }
        return A.length+1;
    }
}
