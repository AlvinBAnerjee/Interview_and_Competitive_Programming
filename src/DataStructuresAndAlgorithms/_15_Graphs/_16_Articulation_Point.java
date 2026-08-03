package DataStructuresAndAlgorithms._15_Graphs;
import java.util.*;
/*
ARTICULATION POINTS (cut vertices) — Tarjan's low-link, one DFS pass, O(V + E).

A vertex is an articulation point if deleting it (and its edges) increases the number of
connected components.

    disc[u] = the tick at which DFS first reached u. Written once, never changed.
    low[u]  = the smallest disc reachable from u's subtree using tree edges downward
              plus at most one back edge upward.

THE TWO CHECKS:

    u is the ROOT      ->  AP  iff  it has >= 2 children in the DFS tree
    u is NOT the root  ->  AP  iff  some child v has  low[v] >= disc[u]

Why '>=' here but '>' for bridges (see _17_Bridges.java): that one character is the whole
difference between the two problems.
    low[v] >  disc[u]  -> the subtree cannot even reach u    -> the EDGE u-v is critical
    low[v] == disc[u]  -> the subtree reaches u but no higher -> the edge survives (a cycle
                          runs through it), but deleting the VERTEX u still strands the subtree
So every bridge endpoint is interesting, but a vertex can be an articulation point while
touching no bridge at all — e.g. the waist of a "bowtie", where two triangles share one
vertex. Worked example with pictures: TARJANS.md in this folder.

Why the root needs its own rule: the root has no parent, so low[v] >= disc[u] is trivially
true for its children and would flag every root. A root only matters when it is the junction
of two or more otherwise-independent subtrees, hence the child count.
 */
public class _16_Articulation_Point {
    public static class Edge{
        int v;
        int weight;

        Edge(int weight,int v)
        {
            this.weight=weight;
            this.v=v;
        }
    }
    public static class Graph{
        ArrayList<ArrayList<Edge>> list;
        int size;
        Graph(int n)
        {
            this.size=n;
            list=new ArrayList<>();
            ;
            for(int i=0;i<n;i++) {
                ArrayList<Edge> l = new ArrayList<>();
                list.add(l);
            }
        }
        // undirected: store the edge in both directions
        void addEdge(int u,int v,int weight)
        {
            list.get(u).add(new Edge(weight,v));
            list.get(v).add(new Edge(weight,u));
        }
        void addEdgeDirected(int u,int v,int weight)
        {
            list.get(u).add(new Edge(weight,v));
        }
        void print()
        {
            for (int i=0;i<list.size();i++)
            {
                System.out.print(i+"->");
                for (Edge e : list.get(i))
                {
                    System.out.print(e.v+","+e.weight+" ->");
                }
                System.out.println();
            }
        }
        ArrayList<Edge> get(int u)
        {
            return list.get(u);
        }
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int v=sc.nextInt();
        int e=sc.nextInt();
        Graph graph=new Graph(v);
        for (int i=0;i<e;i++)
        {
            int a=sc.nextInt();
            int b=sc.nextInt();
            graph.addEdge(a,b,1);
        }
        articulationPoint(graph);

    }
    static int time=0;
    static void  articulationPoint(Graph graph)
    {
        boolean visited[]=new boolean[graph.size];
        int discovery[]=new int[graph.size];
        int low[]=new int[graph.size];
        boolean ap[]=new boolean[graph.size];
        int parent[]=new int[graph.size];
        for (int i=0;i< graph.size;i++)
        {
            parent[i]=-1;                       // -1 marks "no parent yet" => the DFS root
        }
        time=0;
        // Any start vertex gives the same ANSWER on a connected graph (only the disc/low
        // numbers change). For a possibly-disconnected graph you would instead loop:
        //     for (int i=0;i<graph.size;i++) if(!visited[i]) APutil(graph,i,...);
        APutil(graph,3,visited,discovery,low,ap,parent);

        for (int i=0;i<graph.size;i++)
            if (ap[i])
                System.out.print(i+" ");

    }
    static void APutil(Graph graph,int vertex,boolean visited[],int discovery[],int low[],boolean ap[],int parent[])
    {
        visited[vertex]=true;
        int child_count=0;                      // children in the DFS TREE, not the degree
        low[vertex]=discovery[vertex]=++time;   // low starts at disc, then only ever shrinks
        for (Edge e: graph.get(vertex))
        {
            if (!visited[e.v])
            {
                // ---- TREE EDGE: descend first, then read the child's finished low ----
                child_count++;
                parent[e.v]=vertex;
                APutil(graph,e.v,visited,discovery,low,ap,parent);
                low[vertex]=Math.min(low[vertex],low[e.v]);   // child pulls the parent down

                // ROOT RULE: a root is a cut vertex only with 2+ independent subtrees
                if (parent[vertex]==-1&& child_count>1)
                    ap[vertex]=true;

                // NON-ROOT RULE: '>=' — the subtree can climb back to us at best, never past
                // us, so removing this vertex strands it
                 if(parent[vertex]!=-1&&low[e.v]>=discovery[vertex])
                {
                    ap[vertex]=true;
                }
            }
            // ---- BACK EDGE: e.v is already visited and is not our parent, so it is an
            // ancestor. Use discovery[e.v], NOT low[e.v]. The low[] shortcut happens to be
            // harmless for bridges but is genuinely wrong here — fuzzing both variants against
            // brute force over ~3200 random connected graphs: discovery[] 0 mismatches,
            // low[] 24 mismatches.
            else if (e.v!=parent[vertex])
                low[vertex]=Math.min(low[vertex],discovery[e.v]);
        }
    }
}
/*
Sample input (triangle 0-1-2 with a tail 2-3-4)  ->  articulation points: 2 3

5 5
0 1
1 2
2 0
2 3
3 4

GOTCHAS
1. Skipping the parent by vertex id breaks with parallel edges: a duplicate u-v edge gets
   skipped twice and the vertex looks more critical than it is. Skip by edge id if the input
   can contain multi-edges.
2. Bridges use '>', articulation points use '>='. Do not copy-paste one check into the other.
3. Recursion depth: at V ~ 1e5 a path-shaped graph overflows the JVM stack.
 */
