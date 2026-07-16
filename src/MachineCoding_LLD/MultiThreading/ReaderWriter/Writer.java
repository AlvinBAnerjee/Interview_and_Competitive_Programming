package MachineCoding_LLD.MultiThreading.ReaderWriter;

import java.util.concurrent.Semaphore;

public class Writer extends  Thread {
    Semaphore wrt;
    Semaphore mutex;
    String name;
    Data readc;
    Writer(String name, Semaphore mutex, Semaphore wrt, Data readc)
    {
        this.name=name;
        this.mutex=mutex;
        this.wrt=wrt;
        this.readc=readc;
    }
    @Override
    public void run()
    {
        try {

            Thread.sleep((int)(Math.random()*1000));

            wrt.acquire();
            System.out.println("Writer "+name+" writing");
            Thread.sleep((int)(Math.random()*1000));
            wrt.release();
        }
        catch (Exception e){

        }

    }
}
