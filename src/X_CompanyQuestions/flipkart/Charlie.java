package X_CompanyQuestions.flipkart;

public class Charlie {
    public static void main(String[] args) {
        String s1="sample";
        String s2="sample";
        String str=s2+s2;
        if(KMPSearch(str, s1))
            System.out.println("yes");
        else
            System.out.println("No");
    }
    public static boolean KMPSearch(String txt,String pattern)
    {
        int n=txt.length();
        int m=pattern.length();
        int lps[]=lps(pattern);
        int i=0;
        int j=0;
        while (i<n)
        {
            if(txt.charAt(i)==pattern.charAt(j))
            {
                i++;
                j++;
            }
            if(j==m)
                return true;
            if(i<n && txt.charAt(i)!=pattern.charAt(j))
            {
                if(j==0){
                    i++;
                }
                else
                {
                    j=lps[j-1];
                }
            }
        }
        return false;
    }
    static int[] lps(String s) {
        int n=s.length();
        int lps[]=new int[n];
        lps[0]=0;
        int len=0;
        int i=1;
        while(i<n)
        {
            if(s.charAt(len)==s.charAt(i))
            {
                len++;
                lps[i]=len;
                i++;
            }
            else
            {
                if(len==0)
                {
                    lps[i]=0;i++;
                }
                else
                {
                    len=lps[len-1];
                }
            }
        }
        return lps;
    }
}
