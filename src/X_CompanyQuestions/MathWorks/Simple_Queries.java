package X_CompanyQuestions.MathWorks;

import com.sun.source.tree.Tree;

import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Simple_Queries {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n1=sc.nextInt();
        int arr1[]=new int[n1];
        for(int i=0;i<n1;i++)
        {
            arr1[i]=sc.nextInt();
        }
        int n2=sc.nextInt();
        int arr2[]=new int[n2];
        for(int i=0;i<n2;i++)
        {
            arr2[i]=sc.nextInt();
        }

        Arrays.sort(arr1);
        TreeMap<Integer, Integer> map=new TreeMap<>();
        for(int i=0;i<n1;i++)
        {
            map.put(arr1[i], i+1);
        }
        for(int i=0;i<n2;i++)
        {
            arr2[i]=map.get(map.floorKey(arr2[i]));
        }
        System.out.println(Arrays.toString(arr2));
    }
}
