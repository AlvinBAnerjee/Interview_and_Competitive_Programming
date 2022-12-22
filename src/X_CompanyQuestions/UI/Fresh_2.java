package X_CompanyQuestions.UI;

import java.util.Scanner;

public class Fresh_2 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str=sc.nextLine();
        long ans=rec(str,0);
        System.out.println(ans%1000000007);
    }
    public static long rec(String str, int idx)
    {
        if(idx==str.length())
            return 0;
        long sum=0;
        for(int i=idx+1;i<=str.length();i++)
        {
            sum+=Long.parseLong(str.substring(idx,i))+rec(str,i);
        }
        System.out.println(sum);
        return sum;
    }
    /*
    #include <iostream>
#include<bits/stdc++.h>
using namespace std;
int mod=1e9+7;
void find(int i, int n, string &str,int num, int s, long long &ans){
	if(i==n){
		ans+=s%mod;
		return ;
	}


	for(int k=i;k<n;k++){
		num=num*10+(str[k]-'0');
		find(k+1,n,str,0,s+num,ans);
	}
	return ;
}
//24 6 15 123
int main()
{
	int n;
	cin>>n;
	string str=to_string(n);
	long long ans=0;
    find(0,str.length(),str,0,0,ans);
	cout<<ans<<endl;
	return 0;
}
     */
}
