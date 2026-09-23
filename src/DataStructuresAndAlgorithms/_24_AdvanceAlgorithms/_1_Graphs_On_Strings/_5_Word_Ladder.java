package DataStructuresAndAlgorithms._24_AdvanceAlgorithms._1_Graphs_On_Strings;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class _5_Word_Ladder {
    /*
LeetCode 127. Word Ladder

A transformation sequence from word beginWord to word endWord using a
dictionary wordList is a sequence of words such that:
- The first word is beginWord.
- The last word is endWord.
- Only one letter is different between each adjacent pair of words.
- Every word in the sequence (except beginWord) is in wordList.

Given two words, beginWord and endWord, and a dictionary wordList, return the
number of words in the shortest transformation sequence, or 0 if no such
sequence exists.

Example 1:
Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
Output: 5
Explanation: hit -> hot -> dot -> dog -> cog

Example 2:
Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
Output: 0
Explanation: endWord is not in wordList, so no valid transformation exists.

Constraints:
1 <= beginWord.length <= 10
endWord.length == beginWord.length
1 <= wordList.length <= 5000
wordList[i].length == beginWord.length
beginWord, endWord, and wordList[i] consist of lowercase English letters.
beginWord != endWord
All the words in wordList are unique.

Every word is a node; an edge connects two words that differ in exactly one
letter. That graph is never built explicitly — instead, from each word we
generate all 25 * length one-letter variants and check dictionary membership
on the fly. Unweighted shortest path -> plain BFS, counting levels.
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Queue<String> q = new LinkedList<>();
        HashSet<String> set = new HashSet<>(wordList);
        set.remove(beginWord);

        q.add(beginWord);
        int level = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            while (size-- > 0) {
                String top = q.remove();
                if (top.equals(endWord)) return level + 1;

                char word[] = top.toCharArray();

                for (int i = 0; i < word.length; i++) {
                    char ch = word[i];
                    for (char c = 'a'; c <= 'z'; c++) {
                        word[i] = c;
                        String w = new String(word);
                        if (!set.contains(w)) continue;

                        set.remove(w); // never revisit: BFS already found the shortest way here
                        q.add(w);
                    }
                    word[i] = ch;
                }
            }
            level++;
        }
        return 0;
    }
}
