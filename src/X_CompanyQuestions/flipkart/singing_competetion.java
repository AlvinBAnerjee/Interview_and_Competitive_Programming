package X_CompanyQuestions.flipkart;

import java.util.Scanner;

/*The Teacher wants to divide the group of students for singing competition . Each group will have one mic .
If the number of mic is greater than the number of groups then teacher can select group and divide it such that the
largest group among all groups is as small as possible .
Test case 1 :
n = 5 , k =7 ; k is number of groups mics and n is number of groups initially
[10 , 8 , 6 , 3 ,2 ] – > initially number of students in each group .
Output = 6 ;
we can divide the group of 10 into 5 ,5 —> [5,5, 8 ,6 ,3, 2]
again we divide the group of 8 into 4, 4 – > [5 ,5 , 4 ,4 ,6,3 ,2]
The numbers of groups equals to the number of mics Hence the largest group is of size 6 .
Test case 2 :
n = 5 ,k =9
[100 , 80 , 60 ,40 , 30] – > initially number of students in each group .
Output = 40
First divide the group of 100 into 80 , 20 – > [80 ,20 ,80 , 60 , 40 ,30]
Now divide the group of 80 into 40 ,40 --> [40 , 40 , 20 , 80 , 60 ,40 ,30]
again divide the group of 80 into 40, 40 – > [40 , 40 ,20 ,40 ,40 ,60 ,40 ,30]
divide the group of 60 into 30 ,30 --> [40, 40 , 20 , 40 ,40 , 30 ,30 ,40 ,30]
The numbers of groups equals to the number of mics Hence the largest group is of size 40 .
Teastcases:
5
10 8 6 3 2
7
o/p: 6
*/
public class singing_competetion {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        {
            arr[i]=sc.nextInt();

        }
        int mic=sc.nextInt();
        System.out.println(solve(arr,n,mic));
    }
    static int solve(int arr[], int n, int mic)
    {
        int l=0;
        int r=100;
        int candidate=Integer.MAX_VALUE;
        while(l<=r)
        {
            int mid=(l+r)/2;
            if(check(arr, mid, mic))
            {
                candidate=Math.min(mid, candidate);
                r=mid-1;
            }
            else
            {
                l=mid+1;
            }
        }
        return candidate;
    }
    static boolean check(int arr[], int mid, int mic)
    {
        int count=0;
        for(int x: arr)
        {
            int c=(int)Math.ceil((double) x/mid);
            count+=c;
        }
        if(count<=mic)return true;
        return false;
    }
}
