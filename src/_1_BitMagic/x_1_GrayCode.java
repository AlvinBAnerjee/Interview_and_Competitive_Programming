package _1_BitMagic;

import java.util.ArrayList;
import java.util.List;

public class x_1_GrayCode {
    public List<Integer> grayCode(int n) { // gives gray code element at O(1)
        // A Gray code sequence for 'n' bits always contains 2^n elements
        int numElements = 1 << n;
        List<Integer> ans = new ArrayList<>(numElements);

        for (int i = 0; i < numElements; i++) {
            ans.add(i ^ (i >> 1));
        }

        return ans;
    }
}
