package _5_Strings;

public class _1_Check_if_a_String_is_Subsequence_of_Other {
    boolean isSubSequence(String A, String B){
        int i=0;
        int j=0;
        while(j<B.length() && i<A.length())
        {
            if(A.charAt(i)==B.charAt(j))
            {
                i++;
                j++;
            }
            else
            {
                j++;
            }
        }
        if(i!=A.length())
            return false;
        return true;

    }
}
