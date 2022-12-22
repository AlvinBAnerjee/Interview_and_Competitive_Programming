package X_CompanyQuestions.BNY;

import java.util.*;

public class PorfolioBacktesting {
    static int max=0;
    static int solve(int[] arr, int k){
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int sum = 0;
        for(int price : arr){
            if(price > 0)
                sum += price;
            else{
                if(sum + price >= 0){
                    if(minHeap.size() < k){
                        sum += price;
                        minHeap.add(price);
                    }else{
                        if(price > minHeap.peek()){
                            sum -= minHeap.poll();
                            sum += price;
                            minHeap.add(price);
                        }
                    }
                }else {
                    if(minHeap.size() > 0 && price > minHeap.peek()){
                        sum -= minHeap.poll();
                        sum += price;
                        minHeap.add(price);
                    }
                }
            }
        }
        return (minHeap.size() == k)? sum : -1;
    }
    public static void main(String[] args) {

        int[] arr = {-50 ,10, -5, 10, 20, -10, -100, 1000};
        int k = 2;
        System.out.println(solve(arr, k));
    }
    /*
    package Cashfree;

import java.util.PriorityQueue;

public class Portfolio {
    static int solve(int[] arr, int k){
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int sum = 0;
        for(int price : arr){
            if(price > 0)
                sum += price;
            else{
                if(sum + price >= 0){
                    if(minHeap.size() < k){
                        sum += price;
                        minHeap.add(price);
                    }else{
                        if(price > minHeap.peek()){
                            sum -= minHeap.poll();
                            sum += price;
                            minHeap.add(price);
                        }
                    }
                }else {
                    if(minHeap.size() > 0 && price > minHeap.peek()){
                        sum -= minHeap.poll();
                        sum += price;
                        minHeap.add(price);
                    }
                }
            }
        }
        return (minHeap.size() == k)? sum : -1;
    }
    public static void main(String[] args) {
        //int[] arr = {4, -3, 6, -2, -1};
        //int k = 2;
        //int[] arr = {3, -3, -3, 6, 2, -1};
        //int k = 2;
        //int[] arr = {13, -3, -3, 6, -2, -1};
        //int k = 2;
        int[] arr = {-50 ,10, -5, 10, 20, -10, -100, 1000};
        int k = 2;
        System.out.println(solve(arr, k));
    }
}

     */

}




































            
            
            
            
                
