package _0_NumberTheory;

class _2_Reverse_an_Integer {
    public static void main(String[] args) {
        System.out.println(reverse(123));
    }
    public static int reverse(int x) {
        int mul=1;
        if(x<0)
        {
            mul=-1;
            x=x*(-1);
        }
        int reverse=0;
        while (x>0)
        {
            if (reverse>Integer.MAX_VALUE/10)
                return 0;

            reverse=reverse*10+x%10;
            x=x/10;
        }
        return reverse*mul;
    }
}