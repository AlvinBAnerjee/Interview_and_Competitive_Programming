package X_CompanyQuestions.Navi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Lanisters {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        HashMap<Character, Character>  map=new HashMap<>();
        String str=sc.nextLine();
        char ans[]=new char[str.length()];
        int c=0;
        for(int i=0;i< str.length();i++)
        {
            char ch=str.charAt(i);
            if(!map.containsKey(ch))
            {
                map.put(ch, (char)('A'+c++));
            }
            ans[i]=map.get(ch);
        }
        System.out.println(new String(ans));

    }
}
