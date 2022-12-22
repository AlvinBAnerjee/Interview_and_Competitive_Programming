package X_CompanyQuestions.Microsoft;

import java.util.*;
public class Week {
    public static void main(String[] args) {

        HashMap<Integer, String> ItoD=new HashMap<>();
        ItoD.put(0, "sunday");
        ItoD.put(1, "monday");
        ItoD.put(2, "tuesday");
        ItoD.put(3, "wednesday");
        ItoD.put(4, "thursday");
        ItoD.put(5, "friday");
        ItoD.put(6, "saturday");
        HashMap< String, Integer> DtoI=new HashMap<>();
        DtoI.put("sunday",0);
        DtoI.put("monday",1);
        DtoI.put("tuesday",2);
        DtoI.put("wednesday",3);
        DtoI.put("thursday",4);
        DtoI.put("friday",5);
        DtoI.put("saturday",6);
        Scanner sc=new Scanner(System.in);
        String day=sc.nextLine();
        int k=sc.nextInt();
        k=k%7;
        int n=DtoI.get(day)+k;
        n=n%7;
        System.out.println(ItoD.get(n));
    }
}