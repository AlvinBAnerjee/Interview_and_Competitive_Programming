package X_CompanyQuestions.UI;

import java.util.Scanner;

public class Fresh_1 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        int t=sc.nextInt();
        long freq[]=new long[26];

        for(int i=0;i<str.length();i++)
        {
            char ch=str.charAt(i);
            freq[ch-'a']++;
        }

        while(t-->0)
        {
            long temp[]=new long[26];
            for(int i=0;i<25;i++)
            {
                temp[i+1]=freq[i];
            }
            temp[0]+=freq[25];
            temp[1]+=freq[25];
            for(int i=0;i<26;i++)
            {
                freq[i]=temp[i]%1000000007;
            }

        }
        long sum=0;
        for(long x: freq)
            sum=(sum+x)%1000000007;
        sum=sum%1000000007;
        System.out.println(sum);
    }
}
