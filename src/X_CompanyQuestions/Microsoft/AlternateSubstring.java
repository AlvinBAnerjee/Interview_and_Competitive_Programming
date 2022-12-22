package X_CompanyQuestions.Microsoft;

import java.util.*;
public class AlternateSubstring {
    public static void main(String[] args) {

        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        int start=0;
        int count=1;
        char prev='#';
        int res=1;
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if(prev==ch)
            {
                count++;
            }
            else{
                count=1;
                prev=ch;
            }
            if(count==3)
            {
                count=2;
                start=Math.max(i-1,0);
            }
            res=Math.max(res, i-start+1);
        }
        System.out.println(res);
    }
}



