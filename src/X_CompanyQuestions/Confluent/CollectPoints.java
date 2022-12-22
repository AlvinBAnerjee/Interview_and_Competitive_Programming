package X_CompanyQuestions.Confluent;

import java.util.Arrays;
import java.util.Scanner;

public class CollectPoints {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        int player[]=new int[n];
        int points[]=new int[m];
        int min=Integer.MAX_VALUE;
        int max=Integer.MIN_VALUE;
        for(int i=0;i<n;i++)
        {
            player[i]=sc.nextInt();
        }
        for(int i=0;i<m;i++)
        {
            points[i]=sc.nextInt();
            min=Math.min(min, points[i]);
            max=Math.max(max, points[i]);
        }
        Arrays.sort(player);
        Arrays.sort(points);
        System.out.println(Arrays.toString(points));
        System.out.println(Arrays.toString(player));
        int l=0;
        int h=2*(max-min);
        int candidate=Integer.MAX_VALUE;
        while(l<=h)
        {
            int mid=l+(h-l)/2;
            System.out.println(mid+" "+check(mid, player, points,n,m));
            if(check(mid, player, points,n,m))
            {
                candidate=Math.min(candidate, mid);
                h=mid-1;
            }
            else
            {
                l=mid+1;
            }

        }
        System.out.println(candidate);

    }
    public static   boolean check(int mid, int players[], int points[], int n, int m)
    {
        int k=0;
        for(int i=0;i<n;i++)
        {
            if(players[i]> points[k])
            {
                if(players[i]-points[k]>mid)
                {
                    return false;
                }
                int dist=2*(players[i]-points[k]);
                while(k<m && players[i]>=points[k]) {
                    k++;
                    if (k == m - 1) return true;
                }
                if(k==m)return  true;

                    int left=mid-dist;// time left
                    while(k<m && points[k]-players[i]<=left){
                        k++;
                        if(k==m-1) return true;
                    }
                    if(k==m)return  true;
            }

            else{
                int left=mid;
                while(k<m && points[k]-players[i]<=left){
                    k++;
                    if(k==m-1) return true;
                }
                if(k==m)return  true;
            }
        }
        if(k<m)
        return  false;
        return true;
    }
}
/*
2
6
4 70
76 9 74 2 75 73
 */