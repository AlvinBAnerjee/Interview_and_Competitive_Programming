package MachineCoding_LLD.Concurrency_and_Multithreading.CoffeeShop;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Cashier extends Thread{
    Semaphore sem;
    BlockingQueue<String> orders;
    BlockingQueue<String> touchUp;
    Cashier(Semaphore sem, BlockingQueue<String> orders)
    {
        this.sem=sem;
        this.orders=orders;

    }
    public  void run()
    {
        try
        {
            while(true) {
                Thread.sleep((int) (Math.random() * 10000));
                int random = ((int) (Math.random() * 100) % 3);
                if (random == 0)
                    orders.add("Cappuccino");
                else if (random == 1)
                    orders.add("Frappe");
                else
                    orders.add("Mocha");
                System.out.println("New Order received");
                Thread.sleep((int) (Math.random() * 1000));

            }

        }
        catch (Exception e)
        {

        }
    }

}
