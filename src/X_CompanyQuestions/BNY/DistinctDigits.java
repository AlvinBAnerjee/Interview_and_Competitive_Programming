package X_CompanyQuestions.BNY;

import java.util.*;

public class DistinctDigits {
    int arr[]=new int[1000000];
    static boolean repeated_digit(int n)
    {
        HashSet<Integer> a = new HashSet<>();
        int d;
        while (n != 0)
        {
            d = n % 10;
            if (a.contains(d))
                return false;
            a.add(d);
            n /= 10;
        }

        // return 1 if the number has no
        // repeated digit
        return true;
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int prefix[]=new int[1000001];

        for(int i=1;i<=100000;i++)
        {
            prefix[i]=prefix[i-1];
            if(repeated_digit(i))
            {
                prefix[i]++;
            }
        }
        int l=sc.nextInt();
        int r=sc.nextInt();
        System.out.println(prefix[r]-prefix[l-1]);




    }
}
