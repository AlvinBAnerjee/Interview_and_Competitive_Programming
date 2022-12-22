package MultiThreading;

import java.io.IOException;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BlockingQueue<String> queue
                = new ArrayBlockingQueue<String>(100);
        Thread ob1=new Person("Alvin",queue );
        Thread ob2=new Person("Vanshita", queue);
        ob1.start();
        ob2.start();
        ob1.join();
        ob2.join();
        System.out.println(queue.toString());
    }
}
