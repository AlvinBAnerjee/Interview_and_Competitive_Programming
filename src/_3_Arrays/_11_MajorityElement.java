package _3_Arrays;

public class _11_MajorityElement {
    public static void main(String[] args) {
        int arr[]={2,2,2,2,2,1,1,1,1,1,1,2,3,1,1};
        System.out.println(new _11_MajorityElement().majorityElement(arr));
    }
    public int majorityElement(int[] nums) {
        int l=nums.length;
        int majority=nums[0];
        int count=1;
        for (int i=1;i<l;i++)
        {
            if (nums[i]!=majority)
                count--;
            else
                count++;

            if (count==0)
            {
                majority=nums[i];
                count=1;
            }
        }
        count=0;
        for (int i=0;i<nums.length;i++)
            if (nums[i]==majority)
                count++;
        return (count>nums.length/2)?majority:-1;
    }
}
