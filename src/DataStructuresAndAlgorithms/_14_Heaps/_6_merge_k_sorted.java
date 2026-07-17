package _14_Heaps;

import java.lang.reflect.Array;
import java.util.*;

/*
input
3
2
10 20
2
5 15
3
4 9 11
TC=o(n*klogk)
 */
public class _6_merge_k_sorted {
    static class triplet{
        int value;
        int i;
        int j;
        triplet(int value,int i,int j)
        {
            this.value=value;
            this.i=i;
            this.j=j;
        }
    }
    static class compare_value implements Comparator<triplet>
    {
        @Override
        public int compare(triplet o1, triplet o2) {
            return o1.value-o2.value;
        }
    }

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the no of arrays ie k");
        int k=sc.nextInt();
        int total=0;
        int arr[][]=new int[k][];
        for (int i=0;i<k;i++)
        {
            System.out.println("Enter the size of array "+(i+1));
            int n=sc.nextInt();
            arr[i]=new int[n];
            System.out.println("Enter "+n+" numbers");
            for (int j=0;j<n;j++)
            {
                arr[i][j]=sc.nextInt();
                total++;
            }
        }
        System.out.println("The original arrays are:");
        for(int i=0;i<k;i++)
            System.out.println(Arrays.toString(arr[i]));
        int ans[]=sort(arr,k,total);
        System.out.println("The final array is");
        System.out.println(Arrays.toString(ans));

    }
    static int[] sort(int arr[][],int k,int total)
    {
        int ans[]=new int[total];
        int index=0;
        PriorityQueue<triplet> pq=new PriorityQueue<>(1,new compare_value());
        for (int i=0;i<k;i++)
        {
            pq.add(new triplet(arr[i][0],i,0));
        }
        while (!pq.isEmpty())
        {
            triplet min=pq.poll();
            int value= min.value;;
            int i=min.i;
            int j=min.j;
            ans[index++]=value;
            if (j!=(arr[i].length-1))
            {
                pq.add(new triplet(arr[i][j+1],i,j+1));
            }
        }
        return ans;

    }

}
