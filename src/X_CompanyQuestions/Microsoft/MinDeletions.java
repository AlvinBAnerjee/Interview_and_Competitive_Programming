package X_CompanyQuestions.Microsoft;
import java.util.*;
public class MinDeletions {
    public static void main(String[] args) {
        System.out.println(minimumDeletions("YXXXYXY"));
    }
    public static int minimumDeletions(String s) {
        int cnt = 0;
        Deque<Character> stk = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (!stk.isEmpty() && stk.peek() == 'Y' && c == 'X') {
                stk.pop();
                ++cnt;
            }else {
                stk.push(c);
            }
        }
        return cnt;
    }
}
