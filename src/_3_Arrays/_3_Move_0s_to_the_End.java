package _3_Arrays;

import java.util.Arrays;
import java.util.Scanner;

public class _3_Move_0s_to_the_End {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        String str[]=sc.nextLine().split("");
        System.out.println(Arrays.toString(str));

        int pos=0;
        for (int i =0;i<str.length;i++)
        {
            if (!str[i].equals("0"))
            {
                str[pos++]=str[i];
            }
        }
        for (;pos< str.length;pos++)
        {
            str[pos]="0";
        }
        System.out.println(Arrays.toString(str));
    }
}
