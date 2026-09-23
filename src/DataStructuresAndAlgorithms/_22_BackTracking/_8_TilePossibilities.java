package DataStructuresAndAlgorithms._22_BackTracking;

import java.util.Arrays;

public class _8_TilePossibilities {
    /*
You have n tiles, where each tile has one letter tiles[i] printed on it.

Return the number of possible non-empty sequences of letters you can make using the letters printed on those tiles.

Example 1:

Input: tiles = "AAB"
Output: 8
Explanation: The possible sequences are "A", "B", "AA", "AB", "BA", "AAB", "ABA", "BAA".
Example 2:

Input: tiles = "AAABBC"
Output: 188
Example 3:

Input: tiles = "V"
Output: 1

Constraints:

1 <= tiles.length <= 7
tiles consists of uppercase English letters.

Sort so duplicate letters are adjacent, then use the same used[]/!used[k-1]
pruning as Permutation2 so a repeated letter only starts a branch through
its earliest not-yet-used copy. Unlike a permutation count, every choice
(not just full-length ones) is itself a valid sequence, so count++ happens
right when a tile is picked instead of at a length == n base case.
     */
    int count;
    boolean used[];

    public int numTilePossibilities(String tiles) {
        char arr[] = tiles.toCharArray();
        Arrays.sort(arr);
        used = new boolean[arr.length];
        count = 0;
        rec(arr);
        return count;
    }

    public void rec(char arr[]) {
        for (int k = 0; k < arr.length; k++) {
            if (used[k]) continue;
            if (k > 0 && arr[k - 1] == arr[k] && !used[k - 1]) continue;
            used[k] = true;
            count++; // weird but correct
            rec(arr);
            used[k] = false;
        }
    }
}
