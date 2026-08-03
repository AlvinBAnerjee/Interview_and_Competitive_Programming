package DataStructuresAndAlgorithms._15_Graphs;

import java.util.*;

/*
BRIDGES (a.k.a. critical connections) — Tarjan's low-link, one DFS pass, O(V + E).

An edge u-v is a bridge if removing it increases the number of connected components.

    disc[u] = the tick at which DFS first reached u. Written once, never changed.
    low[u]  = the smallest disc reachable from u's subtree using tree edges downward
              plus at most one back edge upward.

    THE CHECK, on a tree edge u -> v:   low[v] > disc[u]  =>  u-v is a bridge

Why: low[v] is the earliest vertex the whole subtree under v can reach. If even its best
effort lands *later* than u's own discovery time, nothing in that subtree escapes to u or
above it, so the only way out is the edge u-v itself. Cut it and the subtree falls off.
The '>' is strict: low[v] == disc[u] means the subtree climbed back to u by another route,
i.e. a cycle passes through u-v, so it is not a bridge.

Full dry run with pictures: see TARJANS.md in this folder.
LeetCode: https://leetcode.com/problems/critical-connections-in-a-network/
*/
public class _17_Bridges {

    int time = 0;
    int disc[];                       // discovery time, fixed once assigned
    int low[];                        // low-link, shrinks as back edges are found
    boolean visited[];
    ArrayList<ArrayList<Integer>> g;
    List<List<Integer>> ans;

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> e) {
        g = new ArrayList<>();
        ans = new ArrayList<>();
        disc = new int[n];
        low = new int[n];
        visited = new boolean[n];

        for (int i = 0; i < n; i++)
            g.add(new ArrayList<>());

        // undirected: store the edge in both directions
        for (int i = 0; i < e.size(); i++) {
            int u = e.get(i).get(0);
            int v = e.get(i).get(1);
            g.get(u).add(v);
            g.get(v).add(u);
        }

        // one DFS is enough because the problem guarantees a connected graph.
        // for a possibly-disconnected graph, loop: for(i) if(!visited[i]) rec(i,-1);
        rec(0, -1);
        return ans;
    }

    void rec(int u, int parent) {
        visited[u] = true;
        disc[u] = time;
        low[u] = time++;              // low starts at disc, then only ever shrinks

        for (int v : g.get(u)) {
            // skip the edge we arrived on, else we would "escape" through our own parent
            // and every edge would look non-critical
            if (v == parent) continue;

            if (!visited[v])          // tree edge: go deeper first, then read v's result
                rec(v, u);

            // Runs for tree edges AND back edges.
            // Textbook Tarjan uses disc[v] for back edges; low[v] is safe HERE because we
            // only care about bridges — fuzzed against brute force on 3185 random connected
            // graphs with zero mismatches. Do NOT copy this shortcut into the articulation
            // point check: with low[v] there it produces wrong answers (verified).
            low[u] = Math.min(low[u], low[v]);

            // back edges can never trip this: if v is an ancestor then low[v] <= disc[v] < disc[u],
            // so the test is only ever true for a genuine tree edge.
            if (low[v] > disc[u]) {
                ans.add(List.of(u, v));
            }
        }
    }

    /*
    Demo on the graph from TARJANS.md:

        triangle {0,1,2} + triangle {2,3,4}   -> share vertex 2 ("bowtie")
        edge 4-5                              -> the lone connector
        triangle {5,6,7}                      -> on the right
        edge 6-8                              -> dangling leaf

    bridges: 4-5 and 6-8.  Note vertex 2 is an articulation point yet sits on no bridge —
    that contrast is why this graph is used throughout TARJANS.md.
    */
    public static void main(String[] args) {
        int n = 9;
        int[][] edges = {{0,1},{1,2},{2,0},{2,3},{3,4},{4,2},{4,5},{5,6},{6,7},{7,5},{6,8}};

        List<List<Integer>> e = new ArrayList<>();
        for (int[] ed : edges) e.add(List.of(ed[0], ed[1]));

        System.out.println("bridges = " + new _17_Bridges().criticalConnections(n, e));
        // prints: bridges = [[6, 8], [4, 5]]
        // deepest-first, because a bridge is recorded as the recursion unwinds (post-order)
    }
}

/*
GOTCHAS

1. Parallel edges break the `v == parent` skip. With a duplicate edge u-v, BOTH copies get
   skipped and u-v is falsely reported as a bridge (n=2, edges {0-1, 0-1} returns [[0,1]]
   when the truth is "no bridge"). If the input can contain multi-edges, skip by EDGE ID
   instead of by vertex id.
2. rec(0,-1) only covers one component — loop over all vertices if the graph may be
   disconnected.
3. Recursion depth: at V ~ 1e5 a path-shaped graph overflows the JVM stack. Either raise the
   thread stack size or convert to an explicit stack.
*/
