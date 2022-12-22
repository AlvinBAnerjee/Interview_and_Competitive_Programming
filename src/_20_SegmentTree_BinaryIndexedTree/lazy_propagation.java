package _20_SegmentTree_BinaryIndexedTree;
/* IMPORTANT: Multiple classes and nested static classes are supported */

/*
 * uncomment this if you want to read input.
//imports for BufferedReader
import java.io.BufferedReader;
import java.io.InputStreamReader;

//import for Scanner and other utility classes
import java.util.*;
*/

// Warning: Printing unwanted or ill-formatted data to output will cause the test cases to fail
import java.util.*;

class TestClass {
    static int st[];
    static int lazy[];
    static int arr[];
    public static void main(String args[] ) throws Exception {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int m=sc.nextInt();
        st=new int[4*n+1];
        lazy=new int[4*n+1];
        arr=new int[n];
        build(arr,0, 0, n-1);
        for(int i=0;i<m;i++)
        {
            int l=sc.nextInt();
            int r=sc.nextInt();
            update(0, st, 0,n-1, l-1 ,r-1, 1);
        }
        for(int i=0;i<n;i++)
        {
            query(0, st, 0, n-1, i,i);
        }
        System.out.println(Arrays.toString(lazy));

        System.out.println(Arrays.toString(arr));

    }
    public static  void build(int arr[], int index, int left, int right)
    {
        if(left==right)
        {
            st[index]=arr[left];
            return;
        }
        int mid=(left+right)/2;
        build(arr, 2*index+1, left, mid);
        build(arr, 2*index+2, mid+1, right);
        st[index]=st[2*index+1]+ st[2*index+2];
    }
    public static int query(int index,int st[], int low, int high, int ql, int qr)
    {
        if(lazy[index]!=0)
        {
            st[index]+=(high-low+1)* lazy[index];
            if(low!=high)
            {
                lazy[2*index+1]+=lazy[index];
                lazy[2*index+2]+=lazy[index];
                lazy[index]=0;
            }
            else
            {
                arr[low]=st[index];
                lazy[index]=0;
            }

        }
        if(ql>high || qr< low)
            return 0;

        if(ql<=low && high<=qr)
        {

            return st[index];
        }
        int mid=(low+high)/2;
        int left=query(2*index+1, st, low  , mid, ql, qr);
        int right=query(2*index+2, st, mid+1,high, ql, qr);
        return left + right;
    }
    public static void update(int index,int st[], int low, int high, int ql, int qr,int value)
    {
        if(lazy[index]!=0)
        {
            st[index]+=(high-low+1)* lazy[index];
            if(low!=high)
            {
                lazy[2*index+1]+=lazy[index];
                lazy[2*index+2]+=lazy[index];
                lazy[index]=0;
            }
            else
            {
                arr[low]=st[index];
                lazy[index]=0;
            }

        }
        if(ql>high || qr< low || high< low)
            return ;

        if(ql<=low && high<=qr)
        {

            st[index]+=(high-low+1)* value;
            if(low!=high)
            {
                lazy[2*index+1]+=value;
                lazy[2*index+2]+=value;
            }
            return ;
        }
        int mid=(low+high)/2;
        update(2*index+1, st, low  , mid, ql, qr, value);
        update(2*index+2, st, mid+1,high, ql, qr, value);
        st[index]=st[2*index+1] + st[2*index+2];
    }
}

public class lazy_propagation {
}
