package X_CompanyQuestions.flipkart;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Flipkart {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int p=sc.nextInt();
        int a=sc.nextInt();
        System.out.println(solve(p,a));

    }
    public static int solve(int p, int a)
    {
        Queue<Integer> q=new LinkedList<>();
        HashSet<Integer> visited=new HashSet<>();
        visited.add(p);
        q.add(p);
        int level=0;
        if(p==a)return level;
        while(!q.isEmpty())
        {
            level++;
            int size=q.size();
            while(size-->0) {
                int top = q.remove();
                if (top == 0) {
                    if (top == a)
                        return level;
                    continue;
                }

                int g = top + top;
                int h = top - top;
                int j = top * top;
                int k = top / top;

                if (!visited.contains(g)) {
                    visited.add(g);
                    if (g == a)
                        return level;
                    q.add(g);
                }
                if (!visited.contains(h)) {
                    visited.add(h);
                    if (h == a)
                        return level;
                    q.add(h);

                }
                if (!visited.contains(j)) {
                    visited.add(j);
                    if (j == a)
                        return level;
                    q.add(j);

                }
                if (!visited.contains(k)) {
                    visited.add(k);
                    if (k == a)
                        return level;
                    q.add(k);

                }
            }

        }
        return -1;

    }

}
