package MachineCoding_LLD.Concurrency_and_Multithreading.ReaderWriter;

import java.util.concurrent.Semaphore;

public class Reader extends  Thread {
    Semaphore wrt;
    Semaphore mutex;
    String name;
    Data readc;
    Reader(String name, Semaphore mutex, Semaphore wrt, Data readc)
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
            mutex.acquire();
            readc.readc++;
            if(readc.readc==1)
                wrt.acquire();
            mutex.release();
            System.out.println("Reader "+name + " reading");
            Thread.sleep((int)(Math.random()*100));
            mutex.acquire();
            readc.readc--;
            if(readc.readc==0)
                wrt.release();
            mutex.release();
        }
        catch (Exception e){

        }

    }
}
