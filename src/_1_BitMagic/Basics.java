package _1_BitMagic;

public class Basics {
    public static void main(String[] args) {
        int a=2;
        int b=6;
        System.out.println(a&b);//0...00010 & 0.....00110 ie 0.........00010
        System.out.println(a|b);//ie 6
        System.out.println(a^b);//ie 4
        System.out.println("**************************");
        System.out.println(a<<1);
        System.out.println(a<<2);
        System.out.println(a>>1);
    }
}
