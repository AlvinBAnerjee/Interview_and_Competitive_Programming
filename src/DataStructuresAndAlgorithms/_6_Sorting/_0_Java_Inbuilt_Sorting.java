package _6_Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class _0_Java_Inbuilt_Sorting {
    static class Student implements Comparable<Student>
    {
        int roll;
        String dept;
        Student(int roll,String dept)
        {
            this.roll=roll;
            this.dept=dept;
        }

        @Override
        public int compareTo(Student o) {
            return this.dept.compareTo(o.dept);
        }

        @Override
        public String toString() {
            return
                    "roll=" + roll +
                            ", dept='" + dept ;
        }

    }

    static class sort_with_roll implements Comparator<Student>
    {
        @Override
        public int compare(Student o1, Student o2) {
            return o1.roll-o2.roll;
        }
    }

    public static void main(String[] args) {
        int intergers[]={1,4,5,2,0,3,2,89,-99,-1,2};
        Arrays.sort(intergers,2,5);//sorting only the subarray
        System.out.println(Arrays.toString(intergers));
        Arrays.sort(intergers);//Dual pivot Quick Sort this is not stable
        System.out.println(Arrays.toString(intergers));
        Student students[]={new Student(1,"ECE"),new Student(1,"CSE"),new Student(1,"EE"),new Student(2,"ECE"),new Student(3,"ECE"),new Student(-6,"CSE")};
        Arrays.sort(students);
        System.out.println(Arrays.toString(students));//this is stable sorting and uses merge sort
        /*
        Now say we want to sort it with respect to only roll no
        we cant so its better to use comparator
         */
        Arrays.sort(students,new sort_with_roll());
        System.out.println(Arrays.toString(students));
        /*
        Now incase we want to sort something different like an arraylist what the fuck do we do
        we use Collections.sort()
         */
        ArrayList<Student> students1=new ArrayList<>();
        students1.add(new Student(-3,"Arts"));
        students1.add(new Student(2,"Maths"));
        students1.add(new Student(2,"Porn"));
        students1.add(new Student(6,"CSE"));
        students1.add(new Student(4,"Arts"));
        students1.add(new Student(8,"Commerce"));
        Collections.sort(students1);
        System.out.println(students1);

        Collections.sort(students1,new sort_with_roll());
        System.out.println(students1);


    }
}
