package _5_Strings;
import  java.util.*;
public class _3_Leftmost_Non_Repeating_Character {
    /*
    Approach:

Make a count array of maximum number of characters(256).
We can initialize all the elements in this array to -1.
And then loop through our string character by character and check
if the array element with this character as index is -1 or not.
If it is -1 then change it to i and if it not -1 then this means that
this character already appeared before, so change it to -2.

In the end all the repeating characters will be changed to -2 and all
non-repeating characters will contain the index where they occur. Now we can just
 loop through all the non-repeating characters and find the minimum index or the first index.
     */

    static char nonRepeatingCharacter(String S)
    {
        int arr[]=new int[26];
        Arrays.fill(arr,-1);
        for(int i=0;i<S.length();i++)
        {
            if(arr[S.charAt(i)-(int)'a']==-1)
            {
                arr[S.charAt(i)-(int)'a']=i;
            }
            else
            {
                arr[S.charAt(i)-(int)'a']=-2;
            }
        }
        int res=Integer.MAX_VALUE;
        for(int i=0;i<26;i++)
        {
            if(arr[i]>=0)
            {
                res=Math.min(res,arr[i]);
            }
        }
        if(res==Integer.MAX_VALUE)
            return '$';
        return S.charAt(res);
    }
}
