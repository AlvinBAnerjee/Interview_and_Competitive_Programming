package _3_Arrays;

/*
maximum=Max(mormal_maximum,circular_max)
where circular max= total_sum-(smallest contiguous subarray)
 */

public class _10_MaximumSubarrayCircular {
    public static void main(String[] args) {
        int arr[] = {-2, 3, -1};
        System.out.println(new _10_MaximumSubarrayCircular().maxSubarraySumCircular(arr));
    }

    public int maxSubarraySumCircular(int[] A) {
        int arr[] = A;
        int max_so_far = Integer.MIN_VALUE;
        int maxEnding = 0;
        int length = 0;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            maxEnding = maxEnding + arr[i % arr.length];
            if (maxEnding > max_so_far) {
                max_so_far = maxEnding;
                length++;
            }
            if (maxEnding < 0) {
                maxEnding = 0;
                length = 0;
            }
            sum = sum + arr[i];
        }
        int min_so_far = Integer.MAX_VALUE;
        int minEnding = 0;
        length = 0;
        for (int i = 0; i < arr.length; i++) {
            minEnding = minEnding + arr[i % arr.length];
            if (minEnding < min_so_far) {
                min_so_far = minEnding;
                length++;
            }
            if (minEnding > 0) {
                minEnding = 0;
                length = 0;
            }
        }
        if (max_so_far < 0) {
            return max_so_far;
        }
        max_so_far = Math.max(max_so_far, (sum - min_so_far));
        return max_so_far;
    }
}
