package MachineCoding_LLD.Concurrency_and_Multithreading.CoffeeShop;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Barista2 extends Thread{
    Semaphore sem; BlockingQueue<String> orders; BlockingQueue<String> touchUp;
    Barista2(Semaphore sem, BlockingQueue<String> touchUp)
    {
        this.sem=sem;
        this.touchUp=touchUp;
    }
    public  void run()
    {
        try
        {
            while(true) {
                Thread.sleep((int) (Math.random() * 1000));
                if(touchUp.isEmpty())continue;
                String o = touchUp.remove();
                System.out.println("Doing Finishing touch up on " + o);
                Thread.sleep((int) Math.random() * 10);
                System.out.println("John your " + o + " is ready");
            }
        }
        catch (Exception e)
        {

        }
    }

}
