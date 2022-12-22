package X_CompanyQuestions.UI;

import java.util.*;

public class _8_Maximum_Earning_Taxi {
    public long maxTaxiEarnings(int n, int[][] rides) {
        n=rides.length;
        int start[]=new int[n];
        int end[]=new int[n];
        int profit[]=new int[n];
        for(int i=0;i<rides.length;i++)
        {
            start[i]=rides[i][0];
            end[i]=rides[i][1];
            profit[i]=end[i]-start[i]+rides[i][2];
        }
        return jobScheduling(start, end, profit);

    }
    public long jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        long[][] jobs = new long[n][3];
        for (int i = 0; i < n; i++) {
            jobs[i] = new long[] {(long)startTime[i], (long)endTime[i], (long)profit[i]};
        }
        Arrays.sort(jobs, (a, b)->(int)a[1] - (int)b[1]);
        TreeMap<Long, Long> dp = new TreeMap<>();
        dp.put((long)0, (long)0);
        for (long[] job : jobs) {
            long cur = dp.floorEntry(job[0]).getValue() + job[2];
            if (cur > dp.lastEntry().getValue())
                dp.put(job[1], cur);
        }
        return dp.lastEntry().getValue();
    }
}
