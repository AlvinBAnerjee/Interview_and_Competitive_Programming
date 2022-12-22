package X_CompanyQuestions.Navi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class RemoveMiddleCharacter {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        System.out.println(solve(str));
    }
    public static String solve(String str)
    {
        HashMap<Character, ArrayList<Integer>>map=new HashMap<>();
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if(!map.containsKey(ch))
            {
                map.put(ch,new ArrayList<>());
            }
            map.get(ch).add(i);
        }
        HashSet<Integer> forbidden=new HashSet<>();
        for(char ch: map.keySet())
        {
            int size=map.get(ch).size();
            if(size%2==0)
            {
                forbidden.add(map.get(ch).get(size/2-1));
                forbidden.add(map.get(ch).get(size/2));
            }
            else
            {
                forbidden.add(map.get(ch).get(size/2));

            }
        }
        int len=str.length()-forbidden.size();
        char ans[]=new char[len];
        int index=0;
        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            if(!forbidden.contains(i))
            {
                ans[index++]=ch;
            }
        }
        return  new String(ans);

    }
}
