package _5_Strings;
import  java.util.*;
public class _10_Longest_Substring_with_Distinct_Characters {
    public int lengthOfLongestSubstring(String s) {
        int n=s.length();
        int prev[]=new int[256];
        Arrays.fill(prev,-1);
        int i=0;
        int res=0;
        for(int j=0;j<n;j++)
        {
            i=Math.max(i,prev[s.charAt(j)]+1);
            int maxEnd=j-i+1;
            res=Math.max(res,maxEnd);
            prev[s.charAt(j)]=j;

        }
        return res;

    }
}
