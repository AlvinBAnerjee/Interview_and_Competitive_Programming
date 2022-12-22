package SystemLab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Assignment_1_Problem_1 {
    static ArrayList<Process> processQueue;
    static int ProcessIdCounter=0;

    static class Process{
        int id;
        int arrival_time;
        int burst_time;
        int remaining;
        int finish_time;
        Process(int id,int arrival_time,int burst_time)
        {
            this.id=id;
            this.arrival_time=arrival_time;
            this.burst_time=burst_time;
            this.remaining=burst_time;
            this.finish_time=Integer.MAX_VALUE;
        }

    }
    static class SortByID implements Comparator<Process>
    {
        @Override
        public  int compare(Process p1,Process p2)
        {
            return p1.id-p2.id;
        }
    }
    static class SortByArrival implements Comparator<Process>
    {
        @Override
        public  int compare(Process p1,Process p2)
        {
            return p1.arrival_time-p2.arrival_time;
        }
    }
    static class SortByPriority implements Comparator<Process>
    {
        @Override
        public  int compare(Process p1,Process p2)
        {
            if (p1.remaining==p2.remaining)
            {
                if (p1.arrival_time==p2.arrival_time)
                {
                    return p1.id-p2.id;
                }
                else
                {
                    return p1.arrival_time-p2.arrival_time;
                }
            }
            else
            {
                return p1.remaining-p2.remaining;
            }
        }
    }


    static int generateIndex()
    {
        return ProcessIdCounter++;
    }
    static void  readInput(int test_case) throws IOException, InterruptedException {
        System.out.print("Reading Files");
        for (int i=0;i<10;i++) {
            Thread.sleep(100);
            System.out.print(".");
        }
        System.out.println();

        System.out.println("\n The Processes are :");
        System.out.println("________________________________________");
        System.out.println("ProcessID |  Arrival_Time |  Burst Time ");
        System.out.println("________________________________________");


        processQueue=new ArrayList<>();
        File text = new File("src/SystemLab/arrival.txt");
        Scanner sc=new Scanner(text);
        for (int i=1;i<test_case;i++)
                sc.nextLine();
            int i=0;
        while (i!=10)
        {
            int id=generateIndex();
            int arrival=sc.nextInt();
            int burst_time=((int)(Math.random()*100))%8+2;
            System.out.println(id+"         |       "+arrival+"       |     "+burst_time);
            processQueue.add(new Process(id,arrival,burst_time));
            processQueue.sort(new SortByArrival());
            i++;
        }
        System.out.println("________________________________________");


    }
    static void schedule() throws InterruptedException {
        System.out.print("\nStarting Scheduling");
        for (int i=0;i<10;i++) {
            Thread.sleep(100);
            System.out.print(".");
        }
        System.out.println();
        int timeCounter=0;
        int index=0;
        int counter=0;
        ArrayList<Integer> ans=new ArrayList<>();
        PriorityQueue<Process> pq=new PriorityQueue<>(10,new SortByPriority());
        while (index<processQueue.size()&&processQueue.get(index).arrival_time<=timeCounter)
        {
            pq.add(processQueue.get(index++));
        }
        while (!(index==processQueue.size()&&pq.isEmpty()))
        {
            if (!pq.isEmpty()) {
                Process temp = pq.poll();
                int id = temp.id;
                int remaining = temp.remaining;
                ans.add(id);

                if ((remaining - 1) > 0) {
                    temp.remaining = remaining - 1;
                    pq.add(temp);

                }
                else
                {
                    temp.finish_time=timeCounter+1;
                }
            }
            else
            {
                ans.add(-1);
            }
            timeCounter++;

            while (index<processQueue.size()&&processQueue.get(index).arrival_time<=timeCounter)
            {
                pq.add(processQueue.get(index++));
            }
        }
        System.out.println("\n_____________________________Gantt Chart __________________________________");
        System.out.print(String.format("%" + 10 + "s","Process ID:"));
        int start=ans.get(0);
        if (start==-1)
            System.out.println("| NOP ");
        else System.out.print("|  P"+start+"  ");
        for (int i=1;i<ans.size();i++)
        {
            int now=ans.get(i);
            if (now!=start)
            {
                start=now;
                System.out.print("|  P"+start+"  ");
            }
        }
        System.out.println("|");
        System.out.println("_______________________________________________________________________________");

        System.out.print(String.format("%" + 10 + "s","Time Stamp:"));

        start=ans.get(0);
        System.out.print(String.format("%" + 1 + "s",""+0));
        for (int i=1;i<ans.size();i++)
        {
            int now=ans.get(i);
            if (now!=start)
            {
                start=now;
                System.out.print(String.format("%" + 7 + "s",""+i));

            }
        }
        System.out.println(String.format("%" + 7 + "s",""+(ans.size())));
        System.out.println("______________________________________________________________________________\n");

    }
    static void analyze()
     {
         System.out.println("\n____________________________________________Analysis ____________________________________________________________");
         System.out.println("Job           arrival_time           burst_time         finishtime       Turn Around Time       Waiting time");
         System.out.println("__________________________________________________________________________________________________________________");
        int total_waiting=0;
        Collections.sort(processQueue,new SortByID());
        for (int i=0;i<processQueue.size();i++)
        {
            Process temp=processQueue.get(i);
            System.out.println("P"+temp.id
                    +String.format("%" + 20 + "s",""+temp.arrival_time)
                    +String.format("%" + 20 + "s",""+temp.burst_time)
                    +String.format("%" + 20 + "s",""+temp.finish_time)
                    +String.format("%" + 20 + "s",""+(temp.finish_time-temp.arrival_time))
                    +String.format("%" + 20 + "s",""+(temp.finish_time-temp.arrival_time-temp.burst_time)));

                    ;
            total_waiting+=(temp.finish_time-temp.arrival_time-temp.burst_time);
        }
        System.out.println("Average Waiting Time: "+((double)total_waiting/10));
        System.out.println("__________________________________________________________________________________________________________________");

     }
    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i=1;i<=2;i++) {
            System.out.println("*******************************************************************************************");
            System.out.println("\n TEST CASE "+i+"\n");
            readInput(i);
            schedule();
            analyze();
            ProcessIdCounter=0;
        }
    }
}
