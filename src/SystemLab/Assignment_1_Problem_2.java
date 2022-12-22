package SystemLab;

import _6_Sorting._0_Java_Inbuilt_Sorting;
import com.sun.jdi.event.ThreadStartEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.Semaphore;

public class Assignment_1_Problem_2 {
    public static void main(String[] args) throws InterruptedException {
        ArrayList<Process> list=new ArrayList<>();
        Semaphore queueMutex=new Semaphore(1);
        Semaphore mutex=new Semaphore(1);
        Semaphore writerLock=new Semaphore(1);
        for (int i=0;i<10;i++)
        {
            list.add(new Process(100+i,queueMutex,mutex,writerLock,"Guest"+i));
            list.get(i).start();
        }
        Scheduler scheduler=new Scheduler(queueMutex,mutex,writerLock,"Scheduler");
        scheduler.start();
    }
}
class myComparator implements Comparator<Request>//used for comparing the Requests
{
    @Override
    public int compare(Request p1, Request p2) {
        return p1.p.id-p2.p.id;
    }
}
class SharedQueue{
    static PriorityQueue<Request> pq=new PriorityQueue<>(10,new myComparator());
}//shared class for accessing the Priority queue
class SharedCounter{
    static int readCount=0;
}//Shared class for knowing the no of Reader Processes currently present
class Request{
    int type;
    Process p;
    int burstTime;
    Request(int type, Process p)
    {
        this.type=type;
        this.p=p;
        this.burstTime=((int)(Math.random()*100));

    }
}// Class for encapsulating a request
class Process extends  Thread{//Creatingg every process as a thread
    Semaphore queueMutex;//Mutex for Accessing the Priority Queue
    Semaphore mutex;//Mutex for accessing Reader Writer Process
    Semaphore writerLock;// Mutex for accessing the Reader Writer Process
    int id;
    public Process(int id,Semaphore queueMutex,Semaphore mutex,Semaphore writerLock, String threadName)
    {
        super(threadName);
        this.id=id;
        this.queueMutex=queueMutex;
        this.mutex=mutex;
        this.writerLock=writerLock;
    }

    @Override
    public void run()
    {
        try {// Handling the exception
            Request request = new Request(0, this);// Creating a request of type Read
            queueMutex.acquire();
            SharedQueue.pq.add(request);//Putting the Request in a PriorityQueue
            queueMutex.release();
            this.suspend();// Putting the Process to sleep
            read(request, id);

            request=new Request(1,this);// Creating a request of type Write
            queueMutex.acquire();
            SharedQueue.pq.add(request);// Putting the request in a Priority Queue
            queueMutex.release();
            this.suspend();// Putting the Process to sleep
            write(request,id);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }


    }
    public void read(Request request,int  ProcessId)//Reader Function
    {
        try {
        mutex.acquire();//Entering Critical Section
        SharedCounter.readCount++;
        if (SharedCounter.readCount==1)
            writerLock.acquire();
        mutex.release();//Exiting the Critical Section
        System.out.println("Guest "+ProcessId +" Performing read operation");
        String message=WeddingCard.greetings;//Accessing the Wedding card
        Thread.sleep(request.burstTime);
        System.out.println("Guest "+ProcessId +" finished read operation");
        mutex.acquire();//Again Entering the Critical Section
        SharedCounter.readCount--;// Decrementing the reader count

            if (SharedCounter.readCount==0)
        {
            writerLock.release();
        }
        mutex.release();

        }catch (InterruptedException e) {
        e.printStackTrace();
    }

    }
    public void write(Request request,int ProcessId)
    {
        try {

            writerLock.acquire();//Entering the Critical Section
            System.out.println("Guest "+ProcessId +" Performing write operation" + writerLock.availablePermits());
            writerLock.release();//Exiting the Critical Section
            System.out.println("Guest "+ProcessId +" finished write operation"+ writerLock.availablePermits());
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
class Scheduler extends Thread{// Another thread to Schedule the Processes according to Priority
    Semaphore queueMutex;
    Semaphore mutex;
    Semaphore writerLock;
    public Scheduler(Semaphore queueMutex,Semaphore mutex,Semaphore writerLock, String threadName)
    {
        super(threadName);
        this.queueMutex=queueMutex;
        this.mutex=mutex;
        this.writerLock=writerLock;
    }
    @Override
    public void run()
    { try {
        int count=0;
        while (count++<1000)
        {

            Request request;
            queueMutex.acquire();//Acquiring the Queue

            Thread.sleep(100);
            if (!SharedQueue.pq.isEmpty()) {//Checking if the queue is Empty
                request = SharedQueue.pq.poll();// Popping the most important Request
                request.p.resume();// Resuming the Process by waking it up
                queueMutex.release();// Releasing the process

            }
            else
            {
                queueMutex.release();
            }
        }


    }catch (InterruptedException e) {
        e.printStackTrace();
    }
    }


}
class WeddingCard{//This is the Wedding Card
    static String greetings="";
    static int readCount=0;
}

