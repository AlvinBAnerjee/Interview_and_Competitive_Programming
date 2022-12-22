package X_CompanyQuestions.Intel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Jerome {
    public static void main(String[] args) {
        int x=213787;
        int y=366666;
        int mul = 1 , temp;
        int rem = x%y;
        if(rem == 0){
            System.out.println(x);
        }
        else {
            do {
                mul = mul * 10;
                temp = rem * mul;
                temp = temp % y;
                if(temp != 0)
                    temp = y-temp;            }
            while (temp >= mul);
            int newNum = x * mul + temp;
            System.out.println(newNum);

        }
        solve(x,y);

    }
    static class Pair{
        String num;
        int rem;
        Pair(String num, int rem)
        {
            this.num=num;
            this.rem=rem;
        }
    }
    public static void solve(int x, int y)
    {
        HashSet<Integer> visited=new HashSet<>();
        if(x%y==0) System.out.println(x);
        Queue<Pair> q=new LinkedList<>();
        q.add(new Pair(""+x, x%y));
        visited.add(x%y);
        while(!q.isEmpty())
        {
            Pair top=q.remove();
            String str=top.num;
            int rem=top.rem;
            if(rem==0)
                System.out.println(str);
            for(int i=0;i<10;i++)
            {
                int r=rem*10+i;
                r=r%y;
                if(visited.contains(r))continue;
                q.add(new Pair(str+i, r));
                visited.add(r);
            }

        }

    }
}
