package X_CompanyQuestions.UI;

import java.util.*;

public class Fresh_3 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int cat[]=new int[n];
        int price[]=new int[n];
        for(int i=0;i<n;i++)
        {
            cat[i]=sc.nextInt();
        }
        for(int i=0;i<n;i++)
        {
            price[i]=sc.nextInt();
        }
        HashMap<Integer, PriorityQueue<Integer>> map=new HashMap<>();
        for(int i=0;i<n;i++)
        {
            if(map.containsKey(cat[i]))
            {
                map.get(cat[i]).add(price[i]);
            }
            else
            {
                map.put(cat[i], new PriorityQueue<>());
                map.get(cat[i]).add(price[i]);
            }
        }
        ArrayList<Integer> temp=new ArrayList<>();
        int res=0;
        for(int x: map.keySet())
        {
            temp.add(map.get(x).remove());
        }
        Collections.sort(temp);
        System.out.println(temp.toString());
        for(int i=0;i<temp.size();i++)
        {
            res+=temp.get(i)*(i+1);
            System.out.println(temp.get(i)+ "  "+(i+1));
        }
        for(int x: map.keySet())
        {
            while(!map.get(x).isEmpty())
                res+=map.size()*map.get(x).remove();
        }
        System.out.println(res);
    }
}
/*
4
3 1 2 3
2 1 4 4
 */

/*
#include <bits/stdc++.h>

using namespace std;
using ll = long long;

ll mod = 1e9 + 7;

ll solve(char c, ll t, vector<vector<ll>>& dp) {
	if(dp[c - 'a'][t] != -1)
		return dp[c - 'a'][t];

	ll offset = 'z' - c;
	if(t - offset <= 0)
		return 1;

	return dp[c - 'a'][t] = solve('a', t - offset - 1, dp) + solve('b', t - offset - 1, dp);
}

int main() {
	string s;
	cin >> s;

	ll t;
	cin >> t;

	vector<vector<ll>> dp(26, vector<ll>(t + 1, -1));
	ll ans = 0;
	for(char c : s) {
		ans += solve(c, t, dp);
		ans %= mod;
	}

	cout << ans << "\n";
}
 */

/*
long long res = 0;
int mod = (1e9+7);
long long findVal(string &temp)
{
	long long val = 0;
	for(char c : temp)
	{
		val = (val*10 + c-'0')%mod;
	}
	return val;
}
void backtrack(int i, long long sum, string &s)
{
	if(i == s.length())
	{
		res += sum;
		return;
	}
	for(int j=i; j<s.length(); j++)
	{
		string temp = s.substr(i,j-i+1);
		long long num = findVal(temp);
		backtrack(j+1,num+sum,s);
	}
}

void solve()
{
	string s;
	cin >> s;
	backtrack(0,0,s);
	cout << res << endl;
}
 */

/*
import java.util.*;

public class MaximumProfit {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] cat = new int[n];
        int[] price = new int[n];
        for (int i = 0; i < n; ++i)
            cat[i] = sc.nextInt();

        for (int i = 0; i < n; ++i)
            price[i] = sc.nextInt();

        long res = maxProfit(cat, price, n);

        System.out.println(res);

        sc.close();
    }

    public static long maxProfit(int[] category, int[] prices, int n) {
        int[][] comb = new int[n][2];

        for (int i = 0; i < n; ++i) {
            comb[i][0] = category[i];
            comb[i][1] = prices[i];
        }

        Arrays.sort(comb, (a, b) -> a[1] - b[1]);

        List<int[]> lookUp = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        List<Integer> res = new ArrayList<>();

        for (int[] c : comb) {
            int cat = c[0];
            int pr = c[1];
            if (!set.contains(cat)) {
                set.add(cat);
                lookUp.add(c);
            } else {
                res.add(pr);
            }
        }

        set.clear();

        int ans = 0;
        for (int[] cat : lookUp) {
            set.add(cat[0]);
            ans += set.size() * cat[1];
        }

        for (int ele : res)
            ans += set.size() * ele;

        return ans;
    }
}
 */