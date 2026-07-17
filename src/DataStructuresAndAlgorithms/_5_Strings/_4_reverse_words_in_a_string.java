package _5_Strings;

public class _4_reverse_words_in_a_string {
    /*
    Given a String S, reverse the string without reversing its individual words. Words are separated by dots.

    Example 1:

    Input:
    S = i.like.this.program.very.much
    Output: much.very.program.this.like.i
    Explanation: After reversing the whole
    string(not individual words), the input
    string becomes
    much.very.program.this.like.i
    Example 2:

    Input:
    S = pqr.mno
    Output: mno.pqr
    Explanation: After reversing the whole
    string , the input string becomes
    mno.pqr

     */
    String reverseWords(String S)
    {
        // code here
        int start=0;
        char str[]=S.toCharArray();
        for(int i=0;i<str.length;i++)
        {
            if(str[i]=='.')
            {
                reverse(start,i-1,str);
                start=i+1;
            }
        }
        reverse(start,str.length-1,str);
        reverse(0,str.length-1,str);
        return new String(str);
    }
    void reverse(int start,int end,char str[])
    {
        int l=start;
        int r=end;
        while(l<r)
        {
            char c=str[l];
            str[l]=str[r];
            str[r]=c;
            l++;
            r--;
        }
    }
}
