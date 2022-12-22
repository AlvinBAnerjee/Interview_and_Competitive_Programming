package X_CompanyQuestions.Amazon;

public class Armour {
    public static void main(String[] args) {
        int power[]={1,2,6,7};
        int s=5;
        int x=rec(power, s, 0,1);
        System.out.println(Math.abs(x)+1);
    }
    public static int rec(int power[], int s, int i, int cap)
    {
        int n=power.length;
        if(i==n)return 0;
        if(cap==1)
        {
            return Math.max(-power[i] + rec(power, s, i+1, cap), -Math.max(0, power[i]-s) + rec(power, s, i+1, 0));
        }
        else
        {
            return -power[i] + rec(power, s, i+1, cap);
        }
    }
}
