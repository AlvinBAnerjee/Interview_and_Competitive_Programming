package X_CompanyQuestions.Navi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class CityTravel {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        HashMap<String, ArrayList<String>> map=new HashMap<>();
        int n=sc.nextInt();
        sc.nextLine();
        for(int i=0;i<n;i++)
        {
            String cityA=sc.next().trim();
            String cityB=sc.nextLine().trim();
            if(!map.containsKey(cityA))
                map.put(cityA,new ArrayList<>());
            if(!map.containsKey(cityB))
                map.put(cityB,new ArrayList<>());
            map.get(cityA).add(cityB);
        }
        for(String x: map.keySet())
        {
            System.out.println(x+" : "+map.get(x).toString());
        }
        String start=sc.nextLine();
        HashSet<String> visited=new HashSet<>();
        rec(start, map,visited);

    }
    public static void rec(String start, HashMap<String, ArrayList<String>> map, HashSet<String> visited)
    {
        visited.add(start);
        System.out.println(start);
        for(String v: map.get(start))
        {
            if(!visited.contains(v))
            {
                rec(v, map, visited);
            }
        }
    }
}
/*
5
Bangalore Hyderabad
Bangalore Chennai
Hyderabad Mumbai
Hyderabad Delhi
Chennai Kerela
Bangalore
 */
