package DataStructuresAndAlgorithms._21_Disjoint_sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class _4_Krushkals {
    int parent[];
    public int find(int x)
    {
        if(x!=parent[x])
            parent[x]=find(parent[x]);
        return parent[x];
    }
    public void union(int u, int v)
    {
        int l = find(u);
        int r = find(v);
        parent[l]=r;
    }
    // Builds MST via Kruskal's, optionally skipping edge `block` or force-including edge `include` (-1 = no-op).
    // edges[i] = {originalIndex, u, v, weight}, e.g. {2, 0, 1, 7} = edge #2 connecting node 0 and node 1 with weight 7.
    // `block`/`include` are positions in `edges` after the sort below, so it must be sorted once before repeated calls.
    // Returns Integer.MAX_VALUE if the graph ends up disconnected.
    public int findMST(int n, int edges[][], int block, int include)
    {
        // Kruskal's picks edges in increasing weight order
        Arrays.sort(edges, (a, b) -> Integer.compare(a[3], b[3]));

        for(int i=0;i<n;i++)
            parent[i]=i;
        int mst=0;
        int edgesCount = 0;
        if(include!=-1)
        {
            int u=edges[include][1];
            int v=edges[include][2];
            int w=edges[include][3];
            union(u,v);
            mst+=w;
            edgesCount++;
        }
        for(int i=0;i<edges.length;i++)
        {
            if(i==block)continue;
            int u=edges[i][1];
            int v=edges[i][2];
            int w=edges[i][3];
            if(find(u)!=find(v))
            {
                mst+=w;
                union(u,v);
                edgesCount++;
            }

            if(edgesCount==n-1)break;
        }
        for (int i = 0; i < n; i++) {
            if (find(i) != find(0))
                return Integer.MAX_VALUE;
        }
        return mst;
    }

}
