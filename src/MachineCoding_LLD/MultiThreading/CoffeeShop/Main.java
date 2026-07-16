package MachineCoding_LLD.MultiThreading.CoffeeShop;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<String> orders
                = new ArrayBlockingQueue<String>(100);
        BlockingQueue<String> touchUp
                =new ArrayBlockingQueue<String>(100);
        Semaphore sem=new Semaphore(1);
        Thread cashier=new Cashier(sem, orders);


            Thread barista1_1 = new Barista1(sem, orders, touchUp);
            Thread barista2_ = new Barista2(sem, touchUp);
            cashier.start();
           barista1_1.start();
           barista2_.start();

    }
}
