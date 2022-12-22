package X_CompanyQuestions.MathWorks;

import java.util.HashMap;

public class IntelligentSubstring {
    public static void main(String[] args) {
       String str="abddcddeddbdbdbd";
       int k=2;
       String alphabet="abcdefghijklmnopqrstuvwxyz";
       String charValue="10101111111111111111111111";

        HashMap<Character, Integer> map=new HashMap<>();
        for(int i=0;i<alphabet.length();i++)
        {
            char ch=alphabet.charAt(i);
            int value=charValue.charAt(i)-'0';
            map.put(ch,value);
        }
        int res=0;
        int start=0;
        int normal=0;
        for(int i=0;i<str.length();i++)
        {
            char ch= str.charAt(i);
            if(map.get(ch)==0)
            {
                normal++;
            }
            while(normal>k)
            {
                if(map.get(str.charAt(start))==0)
                {
                    normal--;
                }
                start++;
            }
            int len=i-start+1;
            if(len> res)
            {
                System.out.println(start+" "+ i);
                res=Math.max(res, len);
            }
        }
        System.out.println(res);
    }
}
