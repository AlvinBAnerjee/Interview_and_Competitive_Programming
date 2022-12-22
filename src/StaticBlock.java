class A
{
    public  void display()
    {
        System.out.println("A");
    }
}
class B extends A{
    public  void display()
    {
        System.out.println("B");
    }
}
public class StaticBlock
{

    public static void main(String[] args)
    {
        A ob1=new B();
        B ob2=new B();
        ob1.display();
        ob2.display();
    }
}