package CNS;

import java.util.Arrays;
import java.util.HashMap;

public class _3_25 {
    public static void main(String[] args) {
        printPossible("L KRSH BRX DUH KDYLQJ D QLFH GDB");//giving the encrypted text
    }
    static void printPossible(String str)
    {
        HashMap<Character,Integer> map =new HashMap<>();//Hash Map to be used for calculating the frequencies of the alphabets
        str=str.toUpperCase();
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if (ch==' '|| ch=='.' || ch=='?')// ignoring the special characters
                continue;
            if (map.containsKey(ch))
            {
                map.put(ch,map.get(ch)+1);//incrementing by one
            }
            else
            {
                map.put(ch,1);
            }
        }
        char keys[]=new char[map.keySet().size()];// the alphabets
        int values[]=new int[map.keySet().size()];// and their respective frequencies
        int i=0;
        for (char ch: map.keySet())
        {
            keys[i]=ch;
            values[i++]=map.get(ch);
        }
        for( i=0;i<keys.length;i++)// sorting the alphabets with respect to frequency
        {
            int max=i;
            for (int j=i;j<keys.length;j++)
            {
                if (values[j]>values[max])
                {
                    max=j;
                }
                int temp1=values[max];
                values[max]=values[i];
                values[i]=temp1;

                char temp2=keys[max];
                keys[max]=keys[i];
                keys[i]=temp2;

            }

        }


        for ( i=0;i<10;i++) {
            int shift =- (int) keys[i]  + (int) 'E';//calculating the initial shift
            shift=shift%26;
            if (shift<0)
                shift=shift+26;//if the shift is negative then convert it to positive shift
            decrypt(str, shift);//calling the decrypt function
        }



    }
    static void decrypt(String str,int key)// Simply the encryption function but this time the shift is negative
    {
        str=str.toUpperCase();//converting the text to upper case
        char ans[]=new char[str.length()];
        for (int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if (ch==' '|| ch=='.' || ch=='?')
                ans[i]=ch;
            else
                ans[i]=(char) (((((int)str.charAt(i)-65)+key)%26)+65);//bring the alphabet to base 0 and then encrypting and converting
        }
        System.out.println(new String(ans));// Printing the decrypted text
    }
}
