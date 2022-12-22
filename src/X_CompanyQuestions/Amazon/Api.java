package X_CompanyQuestions.Amazon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Api {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        HashMap<String, String> registered=new HashMap<>();
        HashSet<String> loggedIn=new HashSet<>();
        int n=sc.nextInt();
        for(int i=0;i<n;i++)
        {
            String command=sc.next();
            System.out.println(command);
            if(command.equals("register"))
            {
                String username=sc.next();
                String password=sc.next();
                if(registered.containsKey(username))
                    System.out.println("Username already Exists");
                else {
                    registered.put(username, password);
                    System.out.println("Registered Successfully");
                }

            }
            else if(command.equals("login"))
            {
                String username=sc.next();
                String password=sc.next();
                if(loggedIn.contains(username))
                    System.out.println("Logged In Unsuccessfully");
                else if(!registered.get(username).equals(password))
                    System.out.println("Logged In Unsuccessfully");
                else
                {
                    loggedIn.add((username));
                    System.out.println("Logged In Successfully");
                }

            }
            else
            {
                String username=sc.next();
                if(!registered.containsKey(username) || !loggedIn.contains(username))
                {
                    System.out.println("Logout Unsuccessful");
                }
                else
                {
                    System.out.println("Logout Successful");
                    loggedIn.remove(username);
                }
            }

            sc.nextLine();
           // System.out.println("x");
        }
    }
}
