package _21_Disjoint_sets;
/*
May take 0(n) for find and union
 */
public class _0_Simple_Implementation {
    static class DisjointSet{
        int parent[];
        DisjointSet(int n)
        {
            parent=new int[n];
            for (int i=0;i<n;i++)
                parent[i]=i;
        }
        int find(int x)
        {
            if (parent[x]==x)
                return x;
            return find(parent[x]);
        }
        void union(int x,int y)
        {
            int x_top=find(x);
            int y_top=find(y);
            if (x_top==y_top)
                return;
            parent[x_top]=y_top;
        }
    }
}
