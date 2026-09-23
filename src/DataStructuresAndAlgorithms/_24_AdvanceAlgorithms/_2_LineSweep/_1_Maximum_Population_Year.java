package DataStructuresAndAlgorithms._24_AdvanceAlgorithms._2_LineSweep;

import java.util.PriorityQueue;

public class _1_Maximum_Population_Year {
    /*
LeetCode 1854. Maximum Population Year

You are given a 2D integer array logs where each logs[i] = [birth_i, death_i]
indicates the birth and death years of the ith person.

The population of some year x is the number of people alive during that year.
The ith person is counted in year x's population if x is in the inclusive range
[birth_i, death_i - 1]. Note that the person is not counted in the year that they die.

Return the earliest year with the maximum population.

Example 1:
Input: logs = [[1993,1999],[2000,2010]]
Output: 1993
Explanation:
The maximum population is 1, and 1993 is the earliest year with this population.

Example 2:
Input: logs = [[1950,1961],[1960,1971],[1970,1981]]
Output: 1960
Explanation:
The maximum population is 2, and it had happened in years 1960 and 1970.
The earlier year between them is 1960.

Constraints:
1 <= logs.length <= 100
1950 <= birth_i < death_i <= 2050
     */
    public int maximumPopulation(int[][] logs) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            if (a[0] == b[0]) return Integer.compare(a[1], b[1]);
            return Integer.compare(a[0], b[0]);
        });

        for (int i = 0; i < logs.length; i++) {
            int a = logs[i][0];
            int b = logs[i][1];
            pq.add(new int[]{a, 1});
            pq.add(new int[]{b, -1});
        }

        int max = 0;
        int year = 0;
        int now = 0;

        while (!pq.isEmpty()) {
            int top[] = pq.remove();
            now = now + top[1];
            if (now > max) {
                max = now;
                year = top[0];
            }
        }
        return year;
    }
}
