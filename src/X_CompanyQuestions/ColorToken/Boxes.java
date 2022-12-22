package X_CompanyQuestions.ColorToken;
import  java.util.*;
public class Boxes {
    static int st[];
    static int lazy[];
    static int arr[];
    public static void main(String args[] ) throws Exception {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int q=sc.nextInt();

        st=new int[4*n+1];
        lazy=new int[4*n+1];
        arr=new int[n];
        build(arr,0, 0, n-1);
        for(int i=0;i<q;i++)
        {
            int type=sc.nextInt();
            if(type==0)
            {
                int l=sc.nextInt()-1;
                int r=sc.nextInt()-1;
                int val=sc.nextInt();
                update(0, st, 0,n-1, l ,r, val);
                for(int x=0;x<n;x++)
                    query(0,st,0,n-1, x,x);
                System.out.println(Arrays.toString(arr));
            }
            else//type 1
            {
                int val=sc.nextInt();
                int l=0;
                int r=n-1;
                int candidate=n;
                while(l<=r)
                {
                    int mid=(l+r)/2;
                    int t=query(0,st,0,n-1, 0, mid);

                    if(val<=t)
                    {
                        r=mid-1;
                        candidate=Math.min(mid, candidate);
                    }
                    else
                    {
                        l=mid+1;
                    }
                }
                System.out.println(candidate+1);
            }
        }for(int x=0;x<n;x++)
            query(0,st,0,n-1, x,x);
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
            else
            {
                arr[low]=st[index];
            }
            return ;
        }
        int mid=(low+high)/2;
        update(2*index+1, st, low  , mid, ql, qr, value);
        update(2*index+2, st, mid+1,high, ql, qr, value);
        st[index]=st[2*index+1] + st[2*index+2];
    }
}
/*
4 5
0 1 2 2
1 3
0 1 1 1
1 3
1 6
 */