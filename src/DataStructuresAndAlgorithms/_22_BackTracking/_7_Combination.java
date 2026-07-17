package _22_BackTracking;

import java.util.ArrayList;
import java.util.List;

public class _7_Combination {
    List<List<Integer>> ans;
    boolean used[];
    public List<List<Integer>> combine(int n, int k) {
        used = new boolean[n];
        ans = new ArrayList<>();
        rec(1, n, k, new ArrayList<>());

        return ans;
    }
    public void rec(int i, int n, int k, List<Integer> temp)
    {
        if(temp.size()==k)
        {
            List<Integer> t = new ArrayList<>();
            t.addAll(temp);
            ans.add(t);
            return;
        }
        if(i>n)return;

        rec(i+1, n ,k, temp);
        temp.add(i);
        rec(i+1, n, k, temp);
        temp.remove(temp.size()-1);
    }
}