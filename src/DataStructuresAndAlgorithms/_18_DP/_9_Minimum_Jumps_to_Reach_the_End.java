package _18_DP;

public class  _9_Minimum_Jumps_to_Reach_the_End {
    public static void main(String[] args) {
        int jumps[]={1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        System.out.println(find_min_jumps(jumps));
    }
    static int find_min_jumps(int jumps[])
    {
        int arr[]=new int[jumps.length];
        arr[0]=0;
        for (int i=1;i<jumps.length;i++)
        {
            arr[i]=Integer.MAX_VALUE;
            for (int j=0;j<i;j++)
            {
                if (j+jumps[j]>=i&&arr[j]!=Integer.MAX_VALUE)
                {
                    arr[i]=Math.min(arr[i],arr[j]+1);
                }
            }
        }
        return arr[jumps.length-1];
    }
}
