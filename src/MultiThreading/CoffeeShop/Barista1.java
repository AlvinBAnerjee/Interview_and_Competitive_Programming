package MultiThreading.CoffeeShop;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Barista1 extends Thread{
    Semaphore sem; BlockingQueue<String> orders; BlockingQueue<String> touchUp;
    Barista1(Semaphore sem, BlockingQueue<String> orders, BlockingQueue<String> touchUp)
    {
        this.sem=sem;
        this.orders=orders;
        this.touchUp=touchUp;
    }
    public  void run()
    {
        try
        {
            while(true) {
                Thread.sleep((int) (Math.random() * 1000));
                if(orders.isEmpty())continue;
                String o = orders.remove();
                sem.acquire();
                System.out.println("Barista preparing " + o);
                Thread.sleep((int) (Math.random() * 100));
                System.out.println("Barista finished " + o + " Stage1");
                sem.release();
                touchUp.add(o);
            }

        }
        catch (Exception e)
        {

        }
    }

}
