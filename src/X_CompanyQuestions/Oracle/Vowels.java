package X_CompanyQuestions.Oracle;

import java.util.HashSet;
import java.util.Scanner;

public class Vowels {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        sc.nextLine();
        int arr[]=new int[n];
        HashSet<Character> map=new HashSet<>();
        map.add('a');
        map.add('e');
        map.add('i');
        map.add('o');
        map.add('u');
        for(int i=0;i<n;i++)
        {
            String str=sc.nextLine();
            char start=str.charAt(0);
            char end=str.charAt(str.length()-1);
            if(map.contains(start) && map.contains(end))
            {
                arr[i]=1;
            }
            else
                arr[i]=0;
        }
        int prefix[]=new int[n+1];
        for(int i=1;i<=n;i++)
        {
            prefix[i]=prefix[i-1]+arr[i-1];
        }
        int q=sc.nextInt();
        for(int i=0;i<q;i++)
        {
            int a=sc.nextInt();
            int b=sc.nextInt();
            System.out.println(prefix[b]-prefix[a-1]);
        }
    }
}
