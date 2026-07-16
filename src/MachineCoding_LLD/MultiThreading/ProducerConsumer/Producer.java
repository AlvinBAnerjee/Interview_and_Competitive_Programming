package MachineCoding_LLD.MultiThreading.ProducerConsumer;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Producer extends Thread{
    String name;
    Semaphore full;
    Semaphore empty;
    Semaphore mutex;
    Queue<Integer> q;
    Producer(String name, Semaphore full,Semaphore empty,Semaphore mutex, Queue<Integer> q)
    {
        this.name=name;
        this.full=full;
        this.empty=empty;
        this.mutex=mutex;
        this.q=q;
    }
    public void run()
    {
        try {
            int i = 0;
            while (true) {
                Thread.sleep((int)(Math.random()*1000));
                empty.acquire();
                mutex.acquire();
                System.out.println(name +" Sending i="+i);
                q.add(i++);
                mutex.release();
                full.release();
            }
        }
        catch (Exception e)
        {
            ;
        }
    }
}
