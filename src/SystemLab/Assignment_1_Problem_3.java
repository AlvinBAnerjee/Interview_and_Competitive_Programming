package SystemLab;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Assignment_1_Problem_3 {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        performRead();//Performing Read
        if (Shared.safeSeqPossible(Shared.processes, Shared.avail, Shared.maxm, Shared.allot)) {//checking if a safe sequence is possible
            Semaphore sem = new Semaphore(1);
            for (int i = 1; i <= Shared.processes.length; i++) {
                Process p = new Process(i, sem);//Creating Process
                p.start();//Starting the process
            }
            Thread.sleep(1000);
            System.out.println("All Process Ended!");
        }
        else
        {
            System.out.println("No Process Scheduled");
        }

    }
    static void performRead() throws InterruptedException, FileNotFoundException {
        System.out.print("Reading Files");
        for (int i=0;i<10;i++) {
            Thread.sleep(100);
            System.out.print(".");
        }
        File text = new File("src/SystemLab/input.txt");
        Scanner sc=new Scanner(text);
        int n=sc.nextInt();
        int m=sc.nextInt();
        Shared.processes=new int[n];
        for (int i=0;i<n;i++)
        {
            Shared.processes[i]=i;
        }

        Shared.avail=new int[m];
        for (int i=0;i<m;i++)
        {
            Shared.avail[i]=sc.nextInt();
        }

        Shared.allot=new int[n][m];
        for (int i=0;i<n;i++)
        {
            for (int j=0;j<m;j++)
            {
                Shared.allot[i][j]=sc.nextInt();
            }
        }

        Shared.maxm=new int[n][m];
        for (int i=0;i<n;i++)
        {
            for (int j=0;j<m;j++)
            {
                Shared.maxm[i][j]=sc.nextInt();
            }
        }
    }

    static class Process extends  Thread{
        int id;
        Semaphore sem;
        Process(int id, Semaphore sem)
        {
            this.id=id;
            this.sem=sem;
        }
        @Override
        public void run(){
            try {
            while (true)
            {

                sem.acquire();
                if (!Shared.canAssign(id))
                {
                    sem.release();

                }
                else
                {
                    Shared.assign(id);
                    sem.release();

                    break;
                }
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    static class Shared{

        static int processes[] ;
        static  int avail[] ;
        static int maxm[][] ;
        static int allot[][] ;

        static void needCalculator(int need[][], int maxReq[][],
                                   int allotedRes[][])//Used for calculating the need matrix for all the processes
        {
            for (int i = 0 ; i < maxReq.length ; i++)
                for (int j = 0 ; j < maxReq[0].length ; j++)
                    need[i][j] = maxReq[i][j] - allotedRes[i][j];
        }

        // Function to find the system is in safe state or not
        static boolean safeSeqPossible(int process[], int currAvail[], int maxReq[][],
                                       int currAllot[][])
        {
            int totalProcess=maxReq.length;
            int totalRes=maxReq[0].length;
            int [][]need = new int[maxReq.length][maxReq[0].length];

            needCalculator(need, maxReq, currAllot);//Calculating the need matrix

            boolean []isFinish = new boolean[totalProcess];
            for(int i=0; i<isFinish.length; i++)
                isFinish[i] = false;//Putting all processes as not finished

            int []work = new int[totalRes];

            int []safeSeq = new int[totalProcess];//Storing the safe sequence

            for (int i = 0; i < totalRes ; i++)
                work[i] = currAvail[i];


            int totalCount = 0;
            while (totalCount < totalProcess)
            {

                boolean found = false;
                for (int p = 0; p < totalProcess; p++)
                {

                    if (isFinish[p] == false)
                    {

                        int j;
                        for (j = 0; j < totalRes; j++)
                            if (need[p][j] > work[j])
                                break;


                        if (j == totalRes)
                        {

                            for (int k = 0 ; k < totalRes ; k++)
                                work[k] += currAllot[p][k];

                            safeSeq[totalCount] = p;
                            totalCount++;

                            isFinish[p] = true;

                            found = true;
                        }
                    }
                }


                if (found == false)
                {
                    System.out.print("No Safe Sequence Found!");
                    return false;
                }
            }


            System.out.print("Possible Safe Sequence Found:");
            for (int i = 0; i < totalProcess ; i++)
                System.out.print(safeSeq[i] + " ");

            return true;
        }

        static boolean canAssign(int process_id)//Check if a process can be assigned the necessary Resources
        {
            int P=maxm.length;
            int R=maxm[0].length;
            int [][]need = new int[maxm.length][maxm[0].length];

            // Function to calculate need matrix
            needCalculator(need, maxm, allot);

            int check=1;
            for (int i=0;i<R;i++)
            {
                if (need[process_id-1][i]>avail[i])
                    check=0;
            }
            if (check==1)
                return true;
            return false;
        }

        static boolean assign(int process_id)//Assigning a process the necessary resources
        {
            int R=maxm[0].length;
            int [][]need = new int[maxm.length][maxm[0].length];

            // Function to calculate need matrix
            needCalculator(need, maxm, allot);
            if (!canAssign((process_id)))
                return false;
            System.out.println("-->Process "+process_id + ":");
            System.out.println("\tAllocated:\t"+Arrays.toString(allot[process_id-1]));
            System.out.println("\tNeeded:\t\t"+Arrays.toString(need[process_id-1]));
            System.out.println("\tAvailable:\t"+Arrays.toString(avail));
            System.out.println("\tResource Allocated!\t\t");
            System.out.println("\tProcess Running!\t\t");
            System.out.println("\tProcess Ended!\t\t");
            System.out.println("\tResources Released!\t\t");

            for (int i=0;i<R;i++)
            {
                avail[i]=avail[i]+allot[process_id-1][i];
                allot[process_id-1][i]=0;
                maxm[process_id-1][i]=0;
            }
            System.out.println("\tNow Available:\t"+Arrays.toString(avail));


            return true;
        }
    }

}

