package X_CompanyQuestions.Amazon;

import java.util.Scanner;
import java.util.TreeSet;

public class AmazonServer {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int locations[]=new int[n];
        for(int i=0;i<n;i++)
        {
            locations[i]=sc.nextInt();
        }
        int m=sc.nextInt();
        int movedFrom[]=new int[m];
        int movedTo[]=new int[m];
        for(int i=0;i<m;i++)
        {
            movedFrom[i]=sc.nextInt();

        }
        for(int i=0;i<m;i++)
        {
            movedTo[i]=sc.nextInt();
        }
        TreeSet<Integer> map=new TreeSet<>();
        for(int x: locations)
        {
            map.add(x);
        }
        System.out.println(map.toString());
        for(int i=0;i<m;i++)
        {
            int x=movedFrom[i];
            int y=movedTo[i];
            map.remove(x);
            map.add(y);
            System.out.println(map.toString());
            //System.out.println(map.size());
        }
        for(int i: map)
        {
            System.out.println(i);
        }
    }
}
/*
4
1 5 2 6
4
1 4 5 7
4 7 1 3

op: 1 2 3 6

4
1 7 6 8
3
1 7 2
2 9 5
    */
