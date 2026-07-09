
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {
        int a=10;
        System.out.println((++a) + " "+(a++));
        ArrayList<Integer> temp = new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            temp.add(i);
        }

        System.out.println();
        temp.stream()
                .filter(n -> n%2==0)
                .map(n-> 2*n-1)
                .collect(Collectors.toList())
                .forEach(System.out::println);
        Parent ob1=new Child();
        Child ob2=new Child();
        Parent.dist();
        ob2.dist();
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(localDateTime + " \n"+localDate + " \n" + zonedDateTime + " \n");
    }

    void display(String s)
    {
        System.out.println(s);
    }


}

interface Parent{
    public static void dist(){
        System.out.println("Damn");
    }
}
class Child implements Parent{
    public static  void dist(){
        System.out.println("Oh no");
    }
}
enum Days{
    Saturday(7),
    Sunday(0);
    int dayNumber;
    Days(int dayNumber)
    {
        this.dayNumber = dayNumber;
    }
}