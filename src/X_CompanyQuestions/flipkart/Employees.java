package X_CompanyQuestions.flipkart;

import java.util.Scanner;

public class Employees {
    /*
    N employees are given having ids from 1 to N. Then there is an array given a[N]
    in which every ith entry corresponds to who is superior of ith employees.
    Now there are p projects and a single employee is working on a single project.
    For every project, some bonus is given to the manager that is as high in hierarchy as
    possible and hasn't gotten a bonus. We had to find at the end which employees didn't get a bonus.
For Ex. Suppose the case is
8 4
0 1 1 2 3 3 4 4
8 4 6 5
Here 0 represents the CEO of company and he isn't included in the bonus scheme.
In this case the ans would be 4 6 7 8 and you can see it by making the tree.
Btw by hierarchy I mean if X reports to Y and Y reports to Z with X being working on a project,
then Z would get the bonus first if he hasn't gotten it yet
     */
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int p=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt()-1;
        }
        int project[]=new int[p];
        for(int i=0;i<p;i++)
            project[i]=sc.nextInt()-1;
        solve(n,p, arr, project);
    }
    public static   void  solve(int n, int p, int arr[], int projects[])
    {
        boolean bonus[]=new boolean[n];
        for(int x: projects)
        {
            int v=x;
            while(arr[v]!=-1 && !bonus[arr[v]])
            {
                v=arr[v];
            }
            bonus[v]=true;

        }
        for(int i=0;i<n;i++)
        {
            if(!bonus[i])
            {
                System.out.print((i+1)+" , ");
            }
        }
    }
}
