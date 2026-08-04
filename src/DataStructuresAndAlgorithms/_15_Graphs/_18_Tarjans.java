package DataStructuresAndAlgorithms._15_Graphs;

import java.util.Arrays;

/*
TODO: implement Tarjan's algorithm for Strongly Connected Components.
Single DFS pass using a low-link array + an explicit stack with an
"on-stack" marker (unlike Kosaraju's two-pass approach in
_12_kosarajus_algo.java). A node u is the root of an SCC when
low[u] == discovery[u]; pop the stack down to u to emit that SCC.

Reference: https://www.geeksforgeeks.org/tarjan-algorithm-find-strongly-connected-components/
Related LeetCode (uses the same low-link technique for bridges instead of
SCCs): https://leetcode.com/problems/critical-connections-in-a-network/

---------------------------------------------------------------------------
Implemented below: LeetCode 2360, Longest Cycle in a Graph — the case where
disc[] alone is enough and no low[] is needed at all. Full write-up with the
dry run and diagram: TARJANS.md, section 9.
*/
public class _18_Tarjans {

    /*
    LC 2360 — https://leetcode.com/problems/longest-cycle-in-a-graph/

    THE INPUT: the array IS the graph. edges[i] is the single node that i points to,
    or -1 if i points nowhere. Index = source, value = destination, n = edges.length.

        edges = [1, 2, 3, 4, 5, 2, 7, 6, 1, -1]
                  ^                         ^
                  edges[0]=1: 0 -> 1        edges[9]=-1: dead end

    Because every node has AT MOST ONE outgoing edge (a "functional graph"), walking
    forward from any node is deterministic: it must end either at -1 or on a node
    already stamped. So every piece of the graph is a rho shape — a tail feeding into
    at most one cycle. With no branching there is nothing for low[] to minimise over,
    which is why a plain timestamp solves this.

    Returns the length of the longest cycle, or -1 if the graph has none. O(n) / O(n).
    */
    public int longestCycle(int[] edges) {
        int n = edges.length;
        int disc[] = new int[n];        // 0 means unvisited, hence time starts at 1
        boolean visited[] = new boolean[n];  // dead: never assigned. disc[] does the
                                             // visited tracking; safe to delete.
        int longest = -1;

        int time = 1;

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;   // never fires (see above); harmless

            int start = i;          // also unused — kept as submitted; safe to delete
            int curr = i;
            int timeNow = time;         // watermark: "any stamp >= this one is MINE"

            // walk forward, stamping, until we fall off the end or hit a stamped node
            while (curr != -1 && disc[curr] == 0) {
                disc[curr] = time++;
                curr = edges[curr];
            }

            // curr == -1            -> ran off a dead end, no cycle
            // disc[curr] >= timeNow -> stamped during THIS walk: we closed a loop on
            //                          ourselves, so it is a genuine new cycle
            // disc[curr] <  timeNow -> stamped by an EARLIER walk: we merged into
            //                          someone else's chain and whatever cycle lies
            //                          down there was already counted. Must not count.
            if (curr != -1 && disc[curr] >= timeNow) {
                // every stamp handed out between disc[curr] and time went to a node
                // on the cycle, so the difference counts them exactly
                int cycle = time - disc[curr];
                longest = Math.max(longest, cycle);
            }
        }
        return longest;
    }

    public static void main(String[] args) {
        _18_Tarjans s = new _18_Tarjans();

        // the worked example from TARJANS.md: tail 0->1 into the 4-cycle 2->3->4->5,
        // a separate 2-cycle 6<->7, node 8 merging into an old chain, node 9 a dead end
        int[] a = {1, 2, 3, 4, 5, 2, 7, 6, 1, -1};
        System.out.println(Arrays.toString(a) + "  -> " + s.longestCycle(a));   // 4

        int[] b = {3, 3, 4, 2, 3};   // LeetCode sample 1: cycle 3 -> 2 -> 4 -> 3
        System.out.println(Arrays.toString(b) + "  -> " + s.longestCycle(b));   // 3

        int[] c = {2, -1, 3, 1};     // LeetCode sample 2: no cycle
        System.out.println(Arrays.toString(c) + "  -> " + s.longestCycle(c));   // -1

        int[] d = {0};               // self loop counts as a cycle of length 1
        System.out.println(Arrays.toString(d) + "  -> " + s.longestCycle(d));   // 1
    }
}
