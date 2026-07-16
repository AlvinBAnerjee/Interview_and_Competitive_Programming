package MachineCoding_LLD.MultiThreading.ReaderWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
        ArrayList<Thread> al1=new ArrayList<>();
        Semaphore mutex=new Semaphore(1);
        Semaphore wrt=new Semaphore(1);
        Data readc=new Data();
        for(int i=0;i<10;i++)
        {
            al1.add(new Reader("Reader "+i, mutex, wrt,readc));
            al1.add(new Writer("Writer "+i,mutex, wrt, readc ));
        }
        for(Thread t: al1)
        {
            t.start();
        }
    }
}
