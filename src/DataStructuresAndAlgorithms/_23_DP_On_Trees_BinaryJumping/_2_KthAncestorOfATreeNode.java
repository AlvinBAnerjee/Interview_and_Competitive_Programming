package _23_DP_On_Trees_BinaryJumping;

import java.util.Arrays;

/*
https://leetcode.com/problems/kth-ancestor-of-a-tree-node/description/

You are given a tree with n nodes numbered 0..n-1, each node's parent given
in parent[] (parent[root] = -1). You must answer many getKthAncestor(node, k)
queries efficiently.

This is a direct application of Binary Jumping, see _1_BinaryJumping.java
for the concept and a worked example.

up[i][j] = the (2^j)-th ancestor of node i, precomputed once in the
constructor in O(n log n). Each query then just walks the bits of k,
jumping in powers of two, in O(log k).

up.length as 32 handles k up to ~2^31 (problem constraints allow k up to n),
so 32 levels is more than enough (2^31 > 5*10^4).
*/
public class _2_KthAncestorOfATreeNode {

    public static void main(String[] args) {
        //         0
        //        / \
        //       1   2
        //      /     \
        //     3       4
        int[] parent = {-1, 0, 0, 1, 2};
        TreeAncestor treeAncestor = new TreeAncestor(5, parent);

        System.out.println(treeAncestor.getKthAncestor(3, 1)); // expect 1
        System.out.println(treeAncestor.getKthAncestor(5 % 5, 2)); // 0's ancestor, expect -1
        System.out.println(treeAncestor.getKthAncestor(4, 2)); // expect 0
    }

    static class TreeAncestor {

        int[][] up;

        public TreeAncestor(int n, int[] parent) {
            up = new int[n][32];
            for (int[] x : up) Arrays.fill(x, -1);

            for (int i = 0; i < n; i++) {
                up[i][0] = parent[i];
            }

            for (int j = 1; j < up[0].length; j++) {
                for (int i = 0; i < n; i++) {
                    if (up[i][j - 1] != -1) {
                        up[i][j] = up[up[i][j - 1]][j - 1];
                    }
                }
            }
        }

        public int getKthAncestor(int node, int k) {
            for (int j = 16; j >= 0; j--) {
                if ((k & (1 << j)) != 0) {
                    node = up[node][j];
                    if (node == -1) return -1;
                }
            }
            return node;
        }
    }
}
