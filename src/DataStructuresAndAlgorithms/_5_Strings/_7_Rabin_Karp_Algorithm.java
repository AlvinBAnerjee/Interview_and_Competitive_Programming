package _5_Strings;

public class    _7_Rabin_Karp_Algorithm {
    static int search(String txt, String pattern, int q)//returns index of first match
    {
        int n=txt.length();
        int m=pattern.length();
        if(m>n)
            return -1;
        int d=256;//for all the ascii values
        int p=0;//hash value for pattern;
        int t=0;//hash value for text;
        int h=1;
        for(int i=1;i<=m-1;i++)// calculating (d^m-1 % q)
        {
            h=(h*d)% q;
        }

        for(int i=0;i<m;i++)
        {
            p=(p*d+pattern.charAt(i))%q;
            t=(t*d+txt.charAt(i))%q;
        }
        for(int i=0;i<=n-m;i++)
        {
            if(t==p)
            {
                int x=0;
                for( x=0;x<m;x++)
                {
                    if(txt.charAt(i+x)!=pattern.charAt(x))
                        break;
                }
                if(x==m)
                    return i;
            }
            if(i<n-m)
            {
                t=((t-h*txt.charAt(i))*d+txt.charAt(i+m))%q;
                if(t<0)
                    t=t+q;
            }

        }
        return -1;
    }
}
