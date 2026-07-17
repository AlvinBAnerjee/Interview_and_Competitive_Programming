package _18_DP;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class  __18_Scramble_String {
    public static boolean isScrambleRecursive(String str1,String str2)
    {
        int n1=str1.length();
        int n2=str2.length();
        if (n1!=n2)
            return false;
        if (n1==0)
            return true;
        if (str1.equals(str2))
            return true;
        char S1[]=str1.toCharArray();
        char S2[]=str2.toCharArray();
        Arrays.sort(S1);
        Arrays.sort(S2);

        for (int i=0;i<n1;i++)
        {
            if (S1[i]!=S2[i])
                return false;
        }
        for (int i=1;i<n1;i++)
        {
            if (isScrambleRecursive(str1.substring(0,i),str2.substring(0,i))
                    &&
                isScrambleRecursive(str1.substring(i,n1),str2.substring(i,n1)))
                return true;
            if (isScrambleRecursive(str1.substring(n1 - i, n1), str2.substring(0, i))
                    &&
                    isScrambleRecursive(str1.substring(0, n1 - i), str2.substring(i, n1)))
                return true;
        }
        return false;
    }
    public static boolean isScrambleTopDown(String s1,String s2)
    {
        HashMap<String,Boolean> hm=new HashMap<>();
        return isScrambleTopDownUtil(hm, s1, s2);
    }
    public static boolean isScrambleTopDownUtil(HashMap<String , Boolean> hm, String str1, String str2)
    {
        if (hm.containsKey(""+str1+" "+str2))
            return hm.get(""+str1+" "+str2);
        if(hm.containsKey(""+str2+" "+str1))
            return hm.get(""+str2+" "+str1);
        int n1=str1.length();
        int n2=str2.length();
        if (n1!=n2)
            return false;
        if (n1==0)
            return true;
        if (str1.equals(str2))
            return true;
        char S1[]=str1.toCharArray();
        char S2[]=str2.toCharArray();
        Arrays.sort(S1);
        Arrays.sort(S2);

        for (int i=0;i<n1;i++)
        {
            if (S1[i]!=S2[i])
                return false;
        }
        for (int i=1;i<n1;i++)
        {
            boolean a,b,c,d;
            if (hm.containsKey(str1.substring(0,i)+" "+str2.substring(0,i)))
                a=hm.get(str1.substring(0,i)+" "+str2.substring(0,i));
            else
                a=isScrambleTopDownUtil(hm,str1.substring(0,i),str2.substring(0,i));

            if (hm.containsKey(str1.substring(i,n1)+" "+str2.substring(i,n1)))
                b=hm.get(str1.substring(i,n1)+" "+str2.substring(i,n1));
            else
                b=isScrambleTopDownUtil(hm,str1.substring(i,n1),str2.substring(i,n1));

            if (hm.containsKey(str1.substring(n1 - i, n1)+" "+ str2.substring(0, i)))
                c=hm.get(str1.substring(n1 - i, n1)+" "+ str2.substring(0, i));
            else
                c=isScrambleTopDownUtil(hm,str1.substring(n1 - i, n1), str2.substring(0, i));

            if (hm.containsKey(str1.substring(0, n1 - i)+" "+ str2.substring(i, n1)))
                d=hm.get(str1.substring(0, n1 - i)+" "+ str2.substring(i, n1));
            else
                d=isScrambleTopDownUtil(hm,str1.substring(0, n1 - i), str2.substring(i, n1));


            if (a&&b) {
                hm.put(str1+" "+str2,true);
                hm.put(str2+" "+str1,true);
                return true;
            };
            if (c && d) {
                hm.put(str1+" "+str2,true);
                hm.put(str2+" "+str1,true);
                return true;
            }
        }
        hm.put(str1+" "+str2,false);
        hm.put(str2+" "+str1,false);
        return false;
    }
}
