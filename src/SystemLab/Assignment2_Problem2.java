package SystemLab;

import java.io.*;
import java.util.Scanner;

public class Assignment2_Problem2 {
    static FileWriter writer  ;
    static  PrintWriter pw;
    public static void main(String[] args) throws IOException {
        File text = new File("src/SystemLab/input.txt");
        writer= new FileWriter("src/SystemLab/output.txt");
        pw=new PrintWriter(writer);
        Scanner sc=new Scanner(text);
        int n_b=sc.nextInt();//no of subjects for btech
        int n_m=sc.nextInt();//no of subjects for Mtech
        int n_p=sc.nextInt();// no of subjects for phd

        int t_b=sc.nextInt();//tray capacity for Btech
        int t_m=sc.nextInt();//tray capacity for Mtech
        int t_p=sc.nextInt();//tray capacity for Phd
        sc.nextLine();

        FIFOScheduler fs=new FIFOScheduler(n_b,t_b);//making a fifo scheduler for Btech
        LFUScheduler lfs=new LFUScheduler(n_m,t_m);// making a least frequently used scheduler for Mtech
        LRUScheduler lrs=new LRUScheduler(n_p,t_p);// making a least recently used scheduler for Phd
        while (sc.hasNext())
        {
            String input=sc.next();
            input=input.toUpperCase();
            int ch=input.charAt(0);
            //now we will check the initals for the subject request for checking in which course they are present
            if (ch=='B')
                fs.makeQuery(input);
            if (ch=='M')
                lfs.makeQuery(input);
            if (ch=='P')
                lrs.makeQuery(input);
        }
        System.out.println("Total misses "+ (FIFOScheduler.miss+LRUScheduler.miss+LFUScheduler.miss));
        fs.displayTray();
        lfs.displayTray();
        lrs.displayTray();
        pw.close();
    }
}
class FIFOScheduler{//Replacement algorithm for Btech
    int no_of_subjects;
    int tray_capacity;
    int tray[];
    int freq[];
    int last_used[];
    int tray_counter=0;
    int fifo_counter=0;
    static int time=0;
    static int miss=0;
    FIFOScheduler(int no_of_subjects,int tray_capacity)//constructor
    {
        this.no_of_subjects=no_of_subjects;
        this.tray_capacity=tray_capacity;
        tray=new int[tray_capacity];
        freq=new int[no_of_subjects];
        last_used=new int[no_of_subjects];
    }
    void makeQuery(String query)//Make query for query for every subject request made
    {
        int subno=Integer.parseInt(query.substring(1))-1;
        int index=search(subno);
        //System.out.println(index);
        if (index==-1)
        {
            miss++;
            if (tray_counter<tray_capacity) {//incase the tray is empty
                tray[tray_counter++] = subno;
                fifo_counter++;
            }
            else//incase the tray is full
            {
                replace(subno);//applying the replacement algorithm
            }
        }

        freq[subno]++;//increasing the frequency count
        last_used[subno]=time;//putting the last time
        time++;
        //displayTray();
    }

    int search(int subno)//searching if the book is present in the tray
    {
        for (int i=0;i<tray_counter;i++)
        {
            if (tray[i]==subno)
                return subno;
        }
        return -1;
    }
    void replace(int subno)//function for replacement algorithm
    {
        for (int i=0;i<tray_capacity-1;i++)
        {
            tray[i]=tray[i+1];
        }
        tray[tray_capacity-1]=subno;
    }
    void displayTray()//display function for displaying the trays
    {
        System.out.print("Tray content for Btech is:");
        Assignment2_Problem2.pw.println("Tray content for Btech is:");
        for (int i=0;i<tray_counter;i++)
        {
            System.out.print("B"+(tray[i]+1)+"_"+freq[tray[i]]+"|");
            Assignment2_Problem2.pw.print("B"+(tray[i]+1)+"_"+freq[tray[i]]+"|");
        }
        System.out.println();
        Assignment2_Problem2.pw.println();

        int max=0;
        for (int i=0;i< freq.length;i++)
        {
            if (freq[max]<freq[i])
                max=i;
        }
        System.out.println("Most issued subject is:"+"B"+(max+1)+" "+freq[max]+" no of times");
        Assignment2_Problem2.pw.println("Most issued subject is:"+"B"+(max+1)+" "+freq[max]+" no of times");

    }
}
class LFUScheduler{
    int no_of_subjects;
    int tray_capacity;
    int tray[];
    int freq[];
    int last_used[];
    int tray_counter=0;
    int fifo_counter=0;
    static int time=0;
    static int miss=0;
    LFUScheduler(int no_of_subjects,int tray_capacity)//constructor
    {
        this.no_of_subjects=no_of_subjects;
        this.tray_capacity=tray_capacity;
        tray=new int[tray_capacity];
        freq=new int[no_of_subjects];
        last_used=new int[no_of_subjects];
    }
    void makeQuery(String query)//used for making the query for the books
    {
        int subno=Integer.parseInt(query.substring(1))-1;
        int index=search(subno);
        //System.out.println(index);
        if (index==-1)
        {
            miss++;
            if (tray_counter<tray_capacity) {//if the tray is empty
                tray[tray_counter++] = subno;
                fifo_counter++;
            }
            else//incase the tray is full
            {
                replace(subno);
            }
        }

        freq[subno]++;
        last_used[subno]=time;
        time++;
        //displayTray();
    }

    int search(int subno)//used for checking if the book is present in the tray
    {
        for (int i=0;i<tray_counter;i++)
        {
            if (tray[i]==subno)
                return subno;
        }
        return -1;
    }
    void replace(int subno)//applying the replacement algorithm
    {
        int min=0;
        for (int i=0;i<tray_capacity;i++)
        {
            if (freq[tray[i]]<freq[tray[min]])
                min=i;
        }
        tray[min]=subno;
    }
    void displayTray()//display function for displaying the trays
    {
        System.out.print("Tray content for  M.Tech is:");
        Assignment2_Problem2.pw.println("Tray content for Btech is:");
        for (int i=0;i<tray_counter;i++)
        {
            System.out.print("M"+(tray[i]+1)+"_"+freq[tray[i]]+"|");
            Assignment2_Problem2.pw.print("M"+(tray[i]+1)+"_"+freq[tray[i]]+"|");
        }
        System.out.println();
        Assignment2_Problem2.pw.println();

        int max=0;
        for (int i=0;i< freq.length;i++)
        {
            if (freq[max]<freq[i])
                max=i;
        }
        System.out.println("Most issued subject is:"+"B"+(max+1)+" "+freq[max]+" no of times");
        Assignment2_Problem2.pw.println("Most issued subject is:"+"B"+(max+1)+" "+freq[max]+" no of times");

    }
}
class LRUScheduler{
    int no_of_subjects;
    int tray_capacity;
    int tray[];
    int freq[];
    int last_used[];
    int tray_counter=0;
    int fifo_counter=0;
    static int time=1;
    static int miss=0;
    LRUScheduler(int no_of_subjects,int tray_capacity)//constructor
    {
        this.no_of_subjects=no_of_subjects;
        this.tray_capacity=tray_capacity;
        tray=new int[tray_capacity];
        freq=new int[no_of_subjects];
        last_used=new int[no_of_subjects];
    }
    void makeQuery(String query)
    {
        int subno=Integer.parseInt(query.substring(1))-1;
        int index=search(subno);
        //System.out.println(index);
        if (index==-1)
        {
            miss++;
            if (tray_counter<tray_capacity) {//incase the tray is empty
                tray[tray_counter++] = subno;
                fifo_counter++;
            }
            else//incase the tray is full
            {
                replace(subno);
            }
        }

        freq[subno]++;
        last_used[subno]=time;
        time++;
        // displayTray();
    }

    int search(int subno)//checking is a book is present in the tray
    {
        for (int i=0;i<tray_counter;i++)
        {
            if (tray[i]==subno)
                return subno;
        }
        return -1;
    }
    void replace(int subno)//replacement algorithm for LRU
    {
        int min=0;
        for (int i=0;i<tray_capacity;i++)
        {
            if (last_used[tray[i]]<last_used[tray[min]])
                min=i;
        }
        tray[min]=subno;
    }
    void displayTray()//display function for displaying the trays
    {
        System.out.print("Tray content for  Phd is:");
        Assignment2_Problem2.pw.println("Tray content for Phd is:");
        for (int i=0;i<tray_counter;i++)
        {
            System.out.print("P"+(tray[i]+1)+"_"+freq[tray[i]]+"|");
            Assignment2_Problem2.pw.print("P"+(tray[i]+1)+"_"+freq[tray[i]]+"|");
        }
        System.out.println();
        Assignment2_Problem2.pw.println();

        int max=0;
        for (int i=0;i< freq.length;i++)
        {
            if (freq[max]<freq[i])
                max=i;
        }
        System.out.println("Most issued subject is:"+"B"+(max+1)+" "+freq[max]+" no of times");
        Assignment2_Problem2.pw.println("Most issued subject is:"+"B"+(max+1)+" "+freq[max]+" no of times");

    }
}