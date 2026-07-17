package _5_Strings;

public class _2_Check_If_Anagram {
    public static boolean isAnagram(String a,String b)
    {

        int count1[]=new int[26];
        int count2[]=new int[26];
        for(int i=0;i<a.length();i++)
        {
            char ch=a.charAt(i);
            count1[ch-97]++;
        }
        for(int i=0;i<b.length();i++)
        {
            char ch=b.charAt(i);
            count2[ch-97]++;
        }

        for(int i=0;i<26;i++)
        {
            if(count1[i]!=count2[i])
                return false;
        }
        return true;

    }
}
