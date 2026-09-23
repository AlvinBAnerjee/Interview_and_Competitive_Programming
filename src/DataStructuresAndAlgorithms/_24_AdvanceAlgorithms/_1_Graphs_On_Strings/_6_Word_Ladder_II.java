package DataStructuresAndAlgorithms._24_AdvanceAlgorithms._1_Graphs_On_Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class _6_Word_Ladder_II {
    /*
LeetCode 126. Word Ladder II

A transformation sequence from word beginWord to word endWord using a
dictionary wordList is a sequence of words such that only one letter differs
between adjacent words, and every word (except beginWord) is in wordList.

Given beginWord, endWord, and wordList, return all the shortest
transformation sequences from beginWord to endWord, or an empty list if no
such sequence exists.

Example 1:
Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
Output: [["hit","hot","dot","dog","cog"],["hit","hot","lot","log","cog"]]

Example 2:
Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
Output: []

Constraints:
1 <= beginWord.length <= 5
endWord.length == beginWord.length
1 <= wordList.length <= 500
wordList[i].length == beginWord.length
beginWord, endWord, and wordList[i] consist of lowercase English letters.
beginWord != endWord
All the words in wordList are unique.

Word Ladder only needs the length of one shortest path, so BFS can mark a
word visited (remove it from the set) the instant it's reached. Word Ladder
II needs every shortest path, so one BFS pass first records, for every word,
its level and the set of predecessors that reach it on a shortest path
(graph: child -> list of parents). A second pass, DFS from endWord back
through graph, walks only edges where levels[prev] + 1 == levels[word] and
reconstructs every path.
     */
    Map<String, List<String>> graph = new HashMap<>();
    Map<String, Integer> levels = new HashMap<>();
    List<List<String>> result = new ArrayList<>();

    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord)) return result;

        bfs(beginWord, endWord, dict);
        dfs(endWord, beginWord, new LinkedList<>());
        return result;
    }

    private void bfs(String beginWord, String endWord, Set<String> dict) {
        Queue<String> q = new LinkedList<>();
        q.add(beginWord);
        levels.put(beginWord, 0);

        while (!q.isEmpty()) {
            String word = q.poll();
            int level = levels.get(word);
            for (String neighbor : getNeighbors(word, dict)) {

                if (!graph.containsKey(neighbor)) {
                    graph.put(neighbor, new ArrayList<>());
                }
                graph.get(neighbor).add(word);

                if (!levels.containsKey(neighbor)) {
                    levels.put(neighbor, level + 1);
                    q.add(neighbor);
                }
            }
        }
    }

    private void dfs(String word, String beginWord, LinkedList<String> path) {
        path.addFirst(word);

        if (word.equals(beginWord)) {
            result.add(new ArrayList<>(path));
        } else {
            List<String> prevWords = graph.get(word);
            if (prevWords != null) {
                for (String prev : prevWords) {
                    if (levels.get(prev) + 1 == levels.get(word)) {
                        dfs(prev, beginWord, path);
                    }
                }
            }
        }

        path.removeFirst();
    }

    private List<String> getNeighbors(String word, Set<String> dict) {
        List<String> neighbors = new ArrayList<>();
        char[] chArr = word.toCharArray();
        for (int i = 0; i < chArr.length; i++) {
            char original = chArr[i];
            for (char c = 'a'; c <= 'z'; c++) {
                chArr[i] = c;
                String newWord = new String(chArr);
                if (dict.contains(newWord) && !newWord.equals(word)) {
                    neighbors.add(newWord);
                }
            }
            chArr[i] = original;
        }
        return neighbors;
    }
}
