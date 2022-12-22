import java.util.*;

import java.util.*;

class CodeChef {
    static class Pair {
        int i;
        int x;
        int y;

        Pair(int i,int a, int b) {
            this.i=i;
            x = a;
            y = b;
        }
    }

    static class witha implements Comparator<Pair> {
        @Override
        public int compare(Pair o1, Pair o2) {
            return (int) (o1.y - o2.y);
        }
    }


    public static void main(String[] args)   {
        Scanner sc=new Scanner(System.in);
        int q=sc.nextInt();
        for (int z=0;z<q;z++)
        {
            int n=sc.nextInt();
            ArrayList<Pair> list=new ArrayList<>();
            sc.nextLine();
            String str1[]=sc.nextLine().split(" ");
            String str2[]=sc.nextLine().split(" ");

            for (int i=0;i<n;i++)
            {
                int a_i=Integer.parseInt(str1[i]);
                int t_i=Integer.parseInt(str2[i]);
                Pair p=new Pair(i,a_i,t_i);
                list.add(p);



            }
            Collections.sort(list,new witha());

            int start=list.get(n-1).y;
            String ans="";
            int count=0;
            for (int i=0;i<list.size();i++)
            {
                Pair p=list.get(i);
                int _i=p.i;
                int a_i= p.x;
                int t_i=p.y;
                if (t_i-a_i>=start)
                {
                    ans=ans+""+(_i+1)+" "+(start-a_i )+" "+start+"\n";
                    start=start+a_i;
                    count++;

                }
            }
            System.out.println(count);
            System.out.print(ans);
        }

    }


}
/*
1
5
31 32 6 13 7
14 50 34 4 31
 */



