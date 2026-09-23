package DataStructuresAndAlgorithms._24_AdvanceAlgorithms._1_Graphs_On_Strings;

import java.util.HashMap;
import java.util.List;

public class _3_Word_Break {
    /*
LeetCode 139. Word Break

Given a string s and a dictionary of strings wordDict, return true if s can
be segmented into a space-separated sequence of one or more dictionary words.

Note that the same word in the dictionary may be reused multiple times in
the segmentation.

Example 1:
Input: s = "leetcode", wordDict = ["leet","code"]
Output: true

Example 2:
Input: s = "applepenapple", wordDict = ["apple","pen"]
Output: true

Example 3:
Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
Output: false

Constraints:
1 <= s.length <= 300
1 <= wordDict.length <= 1000
1 <= wordDict[i].length <= 20
s and wordDict[i] consist of only lowercase English letters.
All the strings of wordDict are unique.
     */
    static class Node {
        boolean isEnd;
        Node next[];

        Node() {
            this.isEnd = false;
            next = new Node[26];
        }
    }

    Node root;
    HashMap<Integer, Boolean> map;

    void insert(String str) {
        Node current = root;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (current.next[ch - 'a'] == null) {
                current.next[ch - 'a'] = new Node();
            }
            current = current.next[ch - 'a'];
        }
        current.isEnd = true;
    }

    public boolean search(String str) {
        Node current = root;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (current.next[ch - 'a'] == null) {
                return false;
            }
            current = current.next[ch - 'a'];
        }
        return current.isEnd;
    }

    public boolean wordBreak(String s, List<String> wordDict) {
        map = new HashMap<>();
        root = new Node();
        for (String str : wordDict)
            insert(str);

        return rec(s, 0);
    }

    public boolean rec(String str, int idx) {
        if (idx == str.length()) return true;
        if (map.containsKey(idx)) return map.get(idx);
        Node current = root;
        for (int i = idx; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (current.next[ch - 'a'] == null) {
                map.put(idx, false);
                return false;
            }
            current = current.next[ch - 'a'];
            if (current.isEnd) { // a dictionary word ends exactly at i
                if (rec(str, i + 1)) {
                    map.put(idx, true);
                    return true;
                }
            }
        }
        map.put(idx, false);
        return false;
    }
}
