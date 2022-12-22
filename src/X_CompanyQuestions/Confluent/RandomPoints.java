package X_CompanyQuestions.Confluent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class RandomPoints {

        static  class Pair {
        int x_;
        int y_;
        Pair(int x_, int y_) {
            this.x_ = x_;
            this.y_ = y_;
        }
    }
        static class compX implements Comparator< Pair > {
            @Override
            public int compare(Pair pairA, Pair pairB) {
                return pairA.x_-pairB.x_;
            }
        }
        static class compY implements Comparator<Pair> {
            @Override
            public int compare(Pair pairA, Pair pairB) {
                return pairA.y_- pairB.y_;
            }

        }
        public   static  long dist (Pair   p1,  Pair p2) {
            return ((long) Math.pow((long)p1.x_-p2.x_,2))  + ((long) Math.pow((long)p1.y_-p2.y_,2));
        }

        public static long closest(Pair[] P ) {
            Arrays.sort(P, 0,P.length,new compX());
            int n=P.length;
            return closestUtil(P, 0, n);
        }

        public static long stripClosest(Pair[] strip, int size, long d) {
            long min=d;
            Arrays.sort(strip, 0, size, new compY());
            int n=size;
            for (int i=0; i<n;i++) {
                for (int j = i+1; (strip[j].y_ - strip[i].y_) < min && j < n;j++) {
                    min=Math.min(min, dist(strip[i], strip[j]));
                }
            }

            return min;
        }


        public static long closestUtil(Pair P[],int l,int r) {

            if ((r - l) <= 3) {
                return BF(P, r);
            }

            int mid = l + (r - l) / 2;
            Pair midPair = P[mid];
            long dl = closestUtil(P, l, mid);
            long dr = closestUtil(P, mid, r);

            long d = Math.min(dl, dr);
            Pair[] strip = new Pair[r];
            int j = 0;
            for (int i = 0; i < r; i++) {
                if (Math.abs(P[i].x_ - midPair.x_) < d) {
                    strip[j] = P[i];
                    j++;
                }
            }
            long x=stripClosest(strip, j, d);
            return Math.min( x,d);
        }

        public static long BF(Pair[] P, int n) {
            long min = Long.MAX_VALUE;
            long curr = 0;

            for (int i = 0; i < n; ++i) {
                for (int j = i + 1; j < n; ++j) {
                    curr = dist(P[i], P[j]);
                    min=Math.min(curr, min);
                }
            }
            return min;
        }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        Pair arr[]=new Pair[n];
        for(int i=0;i<n;i++)
        {
            int x=sc.nextInt();
            int y=sc.nextInt();
            arr[i]=new Pair(x,y);
        }
        System.out.println(closest(arr));
    }
    }



