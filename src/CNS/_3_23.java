package CNS;

import java.util.Locale;

public class _3_23 {
    public static void main(String[] args) {
        encrypt("I hope you are having a nice day");//text to be encrypted
    }
    static void encrypt(String str)
    {
        str=str.toUpperCase();//converting all the letters to upper case
        char ans[]=new char[str.length()];//Array to store the answer
        for (int i=0;i<str.length();i++)
        {
            if (str.charAt(i)!=' ')//not converting white space
            ans[i]=(char) ((str.charAt(i)-65+3)%26+65);// shifting by 3 for Caeser Cipher
            else
                ans[i]=' ';
        }
        System.out.println(new String(ans));//Finally Printing the ans
    }
}
