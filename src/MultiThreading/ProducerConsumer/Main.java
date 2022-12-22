package MultiThreading.ProducerConsumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        Semaphore empty=new Semaphore(10);
        Semaphore full=new Semaphore(0);
        Semaphore mutex=new Semaphore(1);
        Queue<Integer> q=new LinkedList<>();
        Thread ob1=new Producer("Alvin", full, empty,mutex,q );
        Thread ob2=new Consumer("Vanshita", full, empty,mutex,q );
        ob1.start();
        ob2.start();
    }
}
