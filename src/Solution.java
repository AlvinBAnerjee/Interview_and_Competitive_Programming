import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solution {

    // Simple mutable User class
    static class User {
        String name;

        User(String name) {
            this.name = name;
        }
    }

    // 1️⃣ Attempt to swap references (FAILS)
    static void swapUsers(User u1, User u2) {
        User temp = u1;
        u1 = u2;
        u2 = temp;
    }

    // 2️⃣ Mutate object (WORKS)
    static void changeUser(User u) {
        u.name = "Changed";
    }

    // 3️⃣ Fake swap by swapping internal state (WORKS, but NOT a real swap)
    static void fakeSwap(User u1, User u2) {
        String temp = u1.name;
        u1.name = u2.name;
        u2.name = temp;
    }

    public static void main(String[] args) {

        User a = new User("Alice");
        User b = new User("Bob");

        System.out.println("Initial:");
        System.out.println("a = " + a.name);
        System.out.println("b = " + b.name);

        // Try swapping references
        swapUsers(a, b);
        System.out.println("\nAfter swapUsers (reference swap attempt):");
        System.out.println("a = " + a.name);
        System.out.println("b = " + b.name);

        // Mutate object
        changeUser(a);
        System.out.println("\nAfter changeUser(a):");
        System.out.println("a = " + a.name);
        System.out.println("b = " + b.name);

        // Fake swap (state swap)
        fakeSwap(a, b);
        System.out.println("\nAfter fakeSwap (state swap):");
        System.out.println("a = " + a.name);
        System.out.println("b = " + b.name);
        Period period = Period.of(1, 2, 5);
        LocalDate date = LocalDate.of(2022, 11, 20);
        date = date.plus(period);
        System.out.println(date);
        Aminal ob =new Cat();
        System.out.println(ob.a);
        Cat ob2 = new Cat();
        System.out.println(ob2.a);
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println(df.format(2.0));   // 2
        System.out.println(df.format(2.5));
        df = new DecimalFormat("0.00");
        System.out.println(df.format(2.0));   // 2
        System.out.println(df.format(2.5));
        Executors.newCachedThreadPool().submit(()->5);
        Executors.newCachedThreadPool().execute(()->{;});
        Optional<Integer> o = Optional.empty();
        ArrayList<String> s=new ArrayList<>();
        Integer x = 10;
        Integer y= 20;
        swap(x, y);
        System.out.println(x);

    }
    static void swap(Integer a, Integer b)
    {
        Integer x= a;
        a=b;
        b=x;

    }
    static class Aminal{
        int a=10;


    }
    static class Cat extends  Aminal{
        int a=20;
    }
}
