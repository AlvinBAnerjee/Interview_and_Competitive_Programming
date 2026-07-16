package MachineCoding_LLD.Concurrency_and_Multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Person extends  Thread{

    String name;
    BlockingQueue<String> queue;
    Person(String name, BlockingQueue<String> queue)
    {
        this.name=name;
        this.queue=queue;
    }
    @Override
    public void run()
    {
        int i=10;
        while(i-->0) {

            try
            {
                System.out.println(name+" .... "+i);
                queue.add(name+" "+i);
                Thread.sleep(100);
            }
            catch (Exception e)
            {
                ;
            }
        }
    }
}
