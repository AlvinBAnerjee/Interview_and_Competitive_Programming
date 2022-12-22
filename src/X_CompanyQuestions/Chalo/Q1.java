package X_CompanyQuestions.Chalo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Q1 {
    static class Interval {
        int start;
        int bt;
        int idx;

        Interval(int start, int bt, int idx) {
            this.start = start;
            this.bt = bt;
            this.idx = idx;
        }
    }

    static class myComp implements Comparator<Interval> {
        @Override
        public int compare(Interval o1, Interval o2) {
            if (o1.bt == o2.bt) {
                return o1.start - o2.start;
            }
            return o1.bt - o2.bt;
        }

        static class myCompS implements Comparator<Interval> {
            @Override
            public int compare(Interval o1, Interval o2) {
                return o1.start - o2.start;
            }
        }

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int t = sc.nextInt();
            while (t-- > 0) {
                int n = sc.nextInt();
                int k = sc.nextInt();
                int w = sc.nextInt();
                int ans[] = new int[n];
                PriorityQueue<Interval> pq = new PriorityQueue<>(new myCompS());
                PriorityQueue<Interval> pqb = new PriorityQueue<>(new myComp());

                for (int i = 0; i < n; i++) {
                    pq.add(new Interval(sc.nextInt(), sc.nextInt(), i));
                }
                int curr = 0;
                while (!pq.isEmpty()) {
                    Interval top = pq.peek();
                    if(pqb.isEmpty())
                    curr = Math.max(curr, top.start);
                    while (!pq.isEmpty() && (curr >= pq.peek().start)) {
                        pqb.add(pq.remove());
                    }

                    top = pqb.remove();
                    int start = top.start;
                    int bt = top.bt;
                    int idx = top.idx;
                    System.out.println(curr + "____"+ top.start +" "+ top.bt);
                    int waiting=curr - start;
                    int val = (bt * k) - waiting* w;
                    ans[idx] = val;
                    curr=curr+bt;
                    System.out.println("w="+(curr-start)  + " end="+ curr);



                }
                while(!pqb.isEmpty())
                {
                    Interval top = pqb.remove();
                    int start = top.start;
                    int bt = top.bt;
                    int idx = top.idx;
                    System.out.println(curr + "____"+ top.start +" "+ top.bt);
                    int waiting=curr - start;
                    int val = (bt * k) - waiting* w;
                    ans[idx] = val;
                    curr=curr+bt;
                    System.out.println("w="+(curr-start)  + " end="+ curr);
                }
                System.out.println(Arrays.toString(ans));
            }
        }
    }
}
/*
1
5 10 1
3 5
4 2
4 1
10 15
15 2
 */
/*
1
5 5 0
1 1
2 1
3 3
3 2
5 5

 */
/*
1
5 2 0
1 5
1 4
5 1
9 4
13 2

 */