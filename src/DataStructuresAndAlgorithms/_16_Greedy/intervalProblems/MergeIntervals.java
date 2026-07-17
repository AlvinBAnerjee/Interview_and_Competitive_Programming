package _16_Greedy.intervalProblems;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b)->{
            return a[0]-b[0];
        });
        int prev[]=intervals[0];
        List<int[]> merge=new LinkedList<>();

        for(int i=0;i<intervals.length;i++)
        {
            int start=intervals[i][0];
            int end=intervals[i][1];

            if(prev[1]>=start)
            {
                prev[1]=Math.max(prev[1], end);
            }
            else
            {

                merge.add(prev);
                prev=intervals[i];
            }
        }
        merge.add(prev);

        return merge.toArray(new int[merge.size()][]);
    }
}
