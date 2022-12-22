package _21_Disjoint_sets;
/*
TC: Worst case O(logn)
 */
public class _1_Union_By_Rank {
    static class DisjointSet{
        int parent[];
        int rank[];
        DisjointSet(int n)
        {
            parent=new int[n];
            rank=new int[n];
            for (int i=0;i<n;i++) {
                parent[i] = i;
                rank[i]=0;
            }
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
            if (rank[x_top]>rank[y_top])
            {
                parent[y_top]=x_top;
            }
            else if (rank[y_top]>rank[x_top])
            {
                parent[x_top]=y_top;
            }
            else
            {
                parent[x_top]=y_top;
                rank[y_top]++;

            }

        }
    }
}
