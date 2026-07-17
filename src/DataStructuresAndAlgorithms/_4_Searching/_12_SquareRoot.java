package _4_Searching;

public class _12_SquareRoot {
    public static void main(String[] args) {
        System.out.println(findRoot(9));
    }
    static  double findRoot(int n)
    {
        double l=0;
        double r=n;
        while(true)
        {
            double mid=(l+r)/2;
            if (Math.abs(mid*mid-n)<0.0001)
                return mid;
            else if(mid*mid<n)
            {
                l=mid;
            }
            else
            {
                r=mid;
            }
        }
    }
}
