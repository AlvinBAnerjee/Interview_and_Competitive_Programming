package X_CompanyQuestions.Microsoft;

public class Seats {
    public int seats(String A) {
        int count=0;
        int n=A.length();
        for(int i=0;i<n;i++)
        {
            if(A.charAt(i)=='x')
                count++;
        }
        int l=0;
        int r=A.length()-1;
        int res=0;
        while(l<r)
        {
            if(A.charAt(l)=='x' && A.charAt(r)=='x')
            {
                count=count-2;
                res=res%10000003+(r-l-1)%10000003-count%10000003;
                l++;r--;
            }
            else if(A.charAt(l)!='x')
            {
                l++;
            }
            else{
                r--;
            }
        }
        return res%10000003;
    }
}
