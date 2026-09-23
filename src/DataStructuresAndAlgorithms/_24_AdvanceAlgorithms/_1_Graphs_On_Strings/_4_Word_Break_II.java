package DataStructuresAndAlgorithms._24_AdvanceAlgorithms._1_Graphs_On_Strings;

import java.util.ArrayList;
import java.util.List;

public class _4_Word_Break_II {
    /*
LeetCode 140. Word Break II

Given a string s and a dictionary of strings wordDict, add spaces in s to
construct a sentence where each word is a valid dictionary word. Return all
such possible sentences in any order.

Note that the same word in the dictionary may be reused multiple times in
the segmentation.

Example 1:
Input: s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
Output: ["cats and dog","cat sand dog"]

Example 2:
Input: s = "pineapplepenapple", wordDict = ["apple","pen","applepen","pine","pineapple"]
Output: ["pine apple pen apple","pineapple pen apple","pine applepen apple"]

Example 3:
Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
Output: []

Constraints:
1 <= s.length <= 20
1 <= wordDict.length <= 1000
1 <= wordDict[i].length <= 10
s and wordDict[i] consist of only lowercase English letters.
All the strings of wordDict are unique.
     */
    static class Node {
        boolean isEnd;
        Node next[];
        String word;

        Node() {
            isEnd = false;
            next = new Node[26];
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
        curr.word = word;
        curr.isEnd = true;
    }

    List<String> ans;

    public List<String> wordBreak(String s, List<String> wordDict) {
        root = new Node();
        ans = new ArrayList<>();
        for (String x : wordDict)
            insert(x);

        rec(s, 0, new ArrayList<>());
        return ans;
    }

    public void rec(String s, int idx, List<String> temp) {
        if (idx == s.length()) {
            StringBuffer t = new StringBuffer();
            for (String str : temp) {
                t.append(" ");
                t.append(str);
            }
            ans.add(t.toString().trim());
            return;
        }
        Node current = root;

        for (int i = idx; i < s.length(); i++) {
            char ch = s.charAt(i);
            current = current.next[ch - 'a'];
            if (current == null) return;
            if (current.isEnd) {
                temp.add(s.substring(idx, i + 1));
                rec(s, i + 1, temp);
                temp.remove(temp.size() - 1);
            }
        }
    }
}
