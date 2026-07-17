package _5_Strings;

public class _6_Naive_Pattern_Distinct_Characters {
    static void search(String pat, String txt)
    {
        int M = pat.length();
        int N = txt.length();
        int i = 0;

        while (i <= N - M) {
            int j;

            /* For current index i, check for pattern match */
            for (j = 0; j < M; j++)
                if (txt.charAt(i + j) != pat.charAt(j))
                    break;

            if (j == M) // if pat[0...M-1] = txt[i, i+1, ...i+M-1]
            {
                System.out.println("Pattern found at index " + i);
                i = i + M;
            }
            else if (j == 0)
                i = i + 1;
            else
                i = i + j; // slide the pattern by j
        }
    }
}
