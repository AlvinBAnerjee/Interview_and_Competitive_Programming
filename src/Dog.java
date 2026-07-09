import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Mammal {
    static { System.out.println("Hello!"); }
    { System.out.println("Good Afternoon."); }
    Mammal(int x)
    {
        System.out.println("Mammal constructor" + " "+x);
    }
    static void print()
    {
        System.out.println("Mammal constructor" );
    }
}
public class Dog extends Mammal {
//    static void print(){
//        System.out.println("I am the main one.");
//    }
    private String name = "Rex";
    { System.out.println("Name: " + name); }
    private static int i = 0;
    static { System.out.println(i); }
    { i++;
        System.out.println(i); }
    public Dog() {
        super(5);
        System.out.println("Woof!");
    }
    public static void main(String[] args) {
        int[] arr =new int[10];
        Integer[] arr2 =new Integer[1];
        arr2[0]=1;
        System.out.println(List.of(arr));
        System.out.println(List.of(arr2));
        final int i=10;
        char ch=i;

    }
}