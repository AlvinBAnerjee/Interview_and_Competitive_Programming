package DataStructuresAndAlgorithms._24_AdvanceAlgorithms._1_Graphs_On_Strings;

import java.util.ArrayList;
import java.util.List;

public class _2_Word_Search_II {
    /*
LeetCode 212. Word Search II

Given an m x n board of characters and a list of strings words, return all
words on the board.

Each word must be constructed from letters of sequentially adjacent cells,
where adjacent cells are horizontally or vertically neighboring. The same
letter cell may not be used more than once in a word.

Example:
Input: board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]],
       words = ["oath","pea","eat","rain"]
Output: ["eat","oath"]

Constraints:
1 <= m, n <= 12
1 <= words.length <= 3 * 10^4
1 <= words[i].length <= 10
board and words[i] consist only of lowercase English letters.
All the strings of words are unique.

Why a Trie? Searching each word independently with plain Word-Search-style
DFS is O(words * m * n * 4^L). Building one Trie out of all words lets a
single DFS from each cell explore every word sharing that prefix at once —
the DFS depth is now bounded by the Trie, and a dead prefix (no Trie child)
prunes the whole branch instantly instead of failing one word at a time.
     */
    static class Node {
        boolean isEnd;
        Node next[];

        Node() {
            next = new Node[26];
            isEnd = false;
        }
    }

    Node root;

    void insert(String word) {
        Node curr = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (curr.next[ch - 'a'] == null)
                curr.next[ch - 'a'] = new Node();
            curr = curr.next[ch - 'a'];
        }
        curr.isEnd = true;
    }

    List<String> ans;

    public List<String> findWords(char[][] board, String[] words) {
        root = new Node();
        ans = new ArrayList<>();
        for (String x : words)
            insert(x);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                char ch = board[i][j];
                board[i][j] = '.';
                char word[] = new char[20];
                word[0] = ch;
                if (root.next[ch - 'a'] != null)
                    rec(board, i, j, root.next[ch - 'a'], word, 1);
                board[i][j] = ch;
                if (ans.size() == words.length) return ans;
            }
        }
        return ans;
    }

    int x[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public void rec(char board[][], int i, int j, Node curr, char word[], int length) {
        if (curr == null) return;

        if (curr.isEnd) {
            ans.add(new String(word, 0, length));
            curr.isEnd = false; // this Trie word is reported, never emit it again
        }

        for (int k = 0; k < 4; k++) {
            int a = i + x[k][0];
            int b = j + x[k][1];

            if (a >= 0 && b >= 0 && a < board.length && b < board[0].length && board[a][b] != '.') {
                char ch = board[a][b];
                board[a][b] = '.';
                word[length] = ch;
                if (curr.next[ch - 'a'] != null)
                    rec(board, a, b, curr.next[ch - 'a'], word, length + 1);
                board[a][b] = ch;
            }
        }
    }
}
