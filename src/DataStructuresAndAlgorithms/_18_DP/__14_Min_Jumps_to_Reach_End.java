package _18_DP;

public class __14_Min_Jumps_to_Reach_End {
    public int jump(int[] nums) {
        // gfg refer alternate sol
        int jumps[]=new int[nums.length];
        jumps[nums.length-1]=0;
        for(int i=nums.length-2;i>=0;i--)
        {
            jumps[i]=Integer.MAX_VALUE;
            int max_jump=nums[i];
            for(int j=i;j<nums.length&&j<=i+max_jump;j++)
            {
                if(jumps[j]!=Integer.MAX_VALUE)
                {
                    jumps[i]=Math.min(jumps[i],jumps[j]+1);
                }
            }
        }
        return jumps[0];
    }
}
