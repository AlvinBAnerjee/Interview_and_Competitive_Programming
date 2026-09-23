package DataStructuresAndAlgorithms._24_AdvanceAlgorithms._1_Graphs_On_Strings;

public class _1_Word_Search {
    /*
LeetCode 79. Word Search

Given an m x n grid of characters board and a string word, return true if word
exists in the grid.

The word can be constructed from letters of sequentially adjacent cells, where
adjacent cells are horizontally or vertically neighboring. The same letter
cell may not be used more than once.

Example 1:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
Output: true

Example 2:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
Output: true

Example 3:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
Output: false

Constraints:
1 <= m, n <= 6
1 <= word.length <= 15
board and word consist of only lowercase and uppercase English letters.
     */
    int n;
    int m;
    int x[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public boolean exist(char[][] grid, String word) {
        n = grid.length;
        m = grid[0].length;
        if (word.length() > n * m) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == word.charAt(0) && rec(grid, i, j, word, 0)) return true;
            }
        }
        return false;
    }

    public boolean rec(char[][] grid, int i, int j, String word, int idx) {
        if (word.charAt(idx) != grid[i][j]) return false;
        if (idx == word.length() - 1) return true;

        char ch = grid[i][j];
        grid[i][j] = '.'; // mark visited on this path

        for (int k = 0; k < 4; k++) {
            int a = i + x[k][0];
            int b = j + x[k][1];

            if (a >= 0 && b >= 0 && a < n && b < m && grid[a][b] != '.') {
                if (rec(grid, a, b, word, idx + 1)) return true;
            }
        }
        grid[i][j] = ch; // backtrack: unmark so other paths can reuse this cell
        return false;
    }
}
