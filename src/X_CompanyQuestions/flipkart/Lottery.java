package X_CompanyQuestions.flipkart;

import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

public class Lottery {
    /*
    public static boolean helper(String str,String s,int k,int i,int j,boolean mark){
    if(i > str.length() || j > s.length()) return false;
    if(j == s.length()){
        return true;
    }
    if(i == str.length()){
        return false;
    }

    char ch1 = str.charAt(i);
    char ch2 = s.charAt(j);

    boolean flag = false;

    if(ch1 == ch2 || (ch1 == 'a' && ch2 == 'o') || (ch1 == 'o' && ch2 == 'a') || (ch1 == 't' && ch2 == 'l') || (ch1 == 'l' && ch2 == 't')){
        flag = flag || helper(str,s,k,i+1,j+1,true);
    }else if(k > 0  && mark){
        flag = flag || helper(str,s,k-1,i+1,j,true);
    }

    if(!mark)
     flag = flag || helper(str,s,k,i+1,j,false);


    return flag;
}
public static int lotteryTickets(String matchStr, String[] tickets, int k) {

    int count = 0;
    int n = matchStr.length();

    for(String str : tickets){
        if(helper(matchStr,str,k,0,0,false)) count++;
    }
    return count;
}
     */
    static int  dp[][][][][];
    public static void main(String[] args) {

        String arr[]={"abcde","aoc", "actld"};
        int k=1;
        String match="aabacd";
        /*
         String arr[]={"ac","zb", "ybja"};
        int k=1;

        String match="xyzabc";
        */

        System.out.println(lotteryTickets(match, arr,k));
    }
    public static int lotteryTickets(String matchStr, String[] tickets, int k) {

        int count = 0;
        int n = matchStr.length();

        for(String str : tickets){
            dp=new int[matchStr.length()+1][str.length()+1][k+1][2][2];
            for(int x[][][][]: dp)
            {
                for(int y[][][]: x)
                {
                    for (int z[][]: y)
                    {
                        for(int p[]: z)
                        Arrays.fill(p,-1);
                    }
                }
            }
            if(helper(matchStr,str,k,0,0,0,1)==1) count++;
        }
        return count;
    }
    public static int  helper(String str,String s,int k,int i,int j,int mark, int del){
        if(i>str.length() || j> s.length())return 0;
        if(j==s.length())
        {
            return  1;
        }
        if(i==str.length()){
            if(del==1 && j==s.length()-1)
                return 1;
            return 0;
        }
        if(dp[i][j][k][mark][del]!=-1)return  dp[i][j][k][mark][del];
        int flag=0;
        char ch1=str.charAt(i);
        char ch2=s.charAt(j);
        if(ch1==ch2 || ch1=='a' && ch2=='o' || ch1=='o' && ch2=='a'||ch1=='t' && ch2=='l'||ch1=='l' && ch2=='t')
        {
            flag=flag | helper(str, s, k, i+1, j+1, 1,del);
        }
        else if(k>0 && mark==1)
        {
            flag=flag | helper(str, s, k-1, i+1, j,1,del);
        }

        if(mark==0)
        {
            flag=flag | helper(str,s,k,i+1,j,0,del);
        }
        if(del==1)
        {
            flag=flag | helper(str,s,k,i,j+1,mark,0);
        }
        return  dp[i][j][k][mark][del]=flag;
    }

}
/*
if(dp[i][j][k][flag] != -1) return dp[i][j][k][flag];
bool ans = false;
if(drawString[i] == ticket[j] || (drawString[i] == 'a' && ticket[j] == 'o') || (drawString[i] == 'o' && ticket[j] == 'a') ||
(drawString[i] == 't' && ticket[j] == 'l') || (drawString[i] == 'l' && ticket[j] == 't')) {
    ans = ans || f(drawString, ticket, i - 1, j - 1, k, 1, dp);
} else if(k > 0 && !flag) {
    ans = ans || f(drawString, ticket, i - 1, j, k - 1, 1, dp);
} else if(flag) {
    ans = ans || f(drawString, ticket, i, j - 1, k, 1, dp);
}

if(flag && drawString.length() >= ticket.length()) {
    ans = ans || f(drawString, ticket, i - 1, j, k, 0, dp);
} else {
    ans = ans || f(drawString, ticket, i, j - 1, k, 0, dp);
}

return dp[i][j][k][flag] = ans;
 */