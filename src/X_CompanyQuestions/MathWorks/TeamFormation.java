package X_CompanyQuestions.MathWorks;
import java.util.*;
public class TeamFormation {
    static  class Pair{
        int value;
        int index;
        Pair(int value, int index)
        {
            this.value=value;
            this.index=index;
        }
    }
    static class myComp implements  Comparator<Pair>
    {
        @Override
        public  int compare(Pair p1, Pair p2)
        {
            if(p1.value==p2.value)
                return p1.index-p2.index;
            return p2.value-p1.value;
        }
    }
    static class myComp2 implements  Comparator<Pair>
    {
        @Override
        public  int compare(Pair p1, Pair p2)
        {
            return p1.value-p2.value;
        }
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();
        }
        int ts=sc.nextInt();
        int m=sc.nextInt();
        int included=0;
        int items_left=n;
        PriorityQueue<Pair> left=new PriorityQueue<>(new myComp());//left max heap
        PriorityQueue<Pair> right=new PriorityQueue<>(new myComp());// right max heap
        boolean visited[]=new boolean[n];
        if(n<=m)
        {
            Arrays.sort(arr);
            for(int i=n-1;i>=n-1-ts;i--)
            {
                System.out.print(arr[i]+" , ");
            }

        }
        else
        {
            int i;
            int j=n-1-m;
            for(i=0;i<m;i++)
            {
                left.add(new Pair(arr[i], i));
                right.add(new Pair(arr[n-1-i], n-1-i));
            }

            while(items_left> m && included<ts)//
            {
                Pair l=left.peek();
                Pair r=right.peek();
                if(visited[l.index]==true)
                {
                    left.remove();
                    continue;
                }
                if(visited[r.index]==true)
                {
                    right.remove();
                    continue;
                }
                if(l.value>= r.value)
                {

                    visited[l.index]=true;
                    left.remove();
                    System.out.print(l.value+ " , ");
                    included++;

                    items_left--;
                   // System.out.println("inc"+included+" left="+ items_left);
                    //System.out.println(Arrays.toString(visited));
                    while(i<arr.length && visited[i]==true)i++;
                    left.add(new Pair(arr[i], i));
                    i++;

                }
                else //(r.value> l.value)
                {
                    visited[r.index]=true;
                    right.remove();
                    System.out.print(r.value+ "  , ");
                    included++;
                    items_left--;
                    //System.out.println("inc"+included+" left="+ items_left);
                    //System.out.println(Arrays.toString(visited));
                    while(j>=0 && visited[j]==true)j--;
                    right.add(new Pair(arr[j], j));
                    j--;
                }



            }
            //System.out.println("*************");
            //Arrays.sort(arr);
            PriorityQueue<Pair> maxH=new PriorityQueue<>(new myComp());
            for(int x=0;x<n;x++)
            {
                maxH.add(new Pair(arr[x], x));
            }
            while(included < ts)
            {
                int x=maxH.remove().index;
                if(visited[x]==false)
                {
                    visited[x]=true;
                    included++;
                    System.out.print( arr[x]+ " , ");
                    //System.out.println(Arrays.toString(visited));
                }
            }



        }
    }
}
/*
9
17
12
10
2
7
2
11
20
8
3
4
 */
