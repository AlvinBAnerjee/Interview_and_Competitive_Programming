package _4_Searching;
/*
Only one element repeats n no of times
TC=O(n)
Space Complexity=O(1)
Do this after Linked List Cycle detection
if space was not a problem simply we could have used a boolean array
Convert it into a graph
 */

public class _7_Repeating_Element_Floyd_Hare {
    public static void main(String[] args) {
        int arr[]={1,3,2,4,6,5,7,3};
        System.out.println(find_repeating_element(arr));

    }
    static int find_repeating_element(int arr[])
    {
        int n=arr.length;
        int slow=arr[0];
        int fast=arr[0];
        do {///for finding if there is a loop
            slow=arr[slow];
            fast=arr[arr[fast]];
        }while (slow!=fast);



        slow=arr[0];
        while (slow!=fast)// for finding the begining of the loop
        {
            slow=arr[slow];
            fast=arr[fast];
        }
        return slow;
    }
}
