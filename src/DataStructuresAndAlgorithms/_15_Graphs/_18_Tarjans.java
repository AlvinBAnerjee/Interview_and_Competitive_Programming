package _15_Graphs;

/*
TODO: implement Tarjan's algorithm for Strongly Connected Components.
Single DFS pass using a low-link array + an explicit stack with an
"on-stack" marker (unlike Kosaraju's two-pass approach in
_12_kosarajus_algo.java). A node u is the root of an SCC when
low[u] == discovery[u]; pop the stack down to u to emit that SCC.

Reference: https://www.geeksforgeeks.org/tarjan-algorithm-find-strongly-connected-components/
Related LeetCode (uses the same low-link technique for bridges instead of
SCCs): https://leetcode.com/problems/critical-connections-in-a-network/
*/
public class _18_Tarjans {
}
