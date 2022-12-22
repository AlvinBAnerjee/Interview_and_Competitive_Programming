package X_CompanyQuestions.RazorPay;
import java.util.*;
public class Tastiness {
    public static void main(String[] args) {
        int arr[]={1,2,3,2,1,4,100, 4,0,-1};
        System.out.println(Arrays.toString(slidingWindow(arr, 3)));;
    }
    static int[] slidingWindow(int weights[], int k){
        int n = weights.length;
        int result[] = new int[n];
        LinkedList<Integer> deq = new LinkedList<>();
        int i=0,j;
        for(j=1;j<=k;j++){
            while(deq.size() != 0 && weights[deq.getLast()] < weights[j])
                deq.removeLast();
            deq.addLast(j);
        }
        result[0] = deq.getFirst();
        for(i=1;i<n;i++,j++){
            if(weights[deq.getFirst()] == weights[i])
                deq.removeFirst();
            while(deq.size() != 0 && j<n && weights[deq.getLast()] < weights[j])
                deq.removeLast();
            if(j<n)
                deq.addLast(j);

            result[i] = (deq.size() == 0)? -1 : deq.getFirst();
        }
        System.out.println(Arrays.toString(result));
        return result;
    }
}
