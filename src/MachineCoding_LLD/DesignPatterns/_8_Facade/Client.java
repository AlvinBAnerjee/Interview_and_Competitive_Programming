package MachineCoding_LLD.DesignPatterns._8_Facade;
interface Menus
{
}
class NonVegMenu implements Menus
{

}
class VegMenu implements Menus
{

}
class Both implements Menus
{

}
 interface Hotel
{
    public Menus getMenus();
}

 class NonVegRestaurant implements Hotel
{
    public Menus getMenus()
    {
        NonVegMenu nv = new NonVegMenu();
        return nv;
    }
}

 class VegRestaurant implements Hotel
{
    public Menus getMenus()
    {
        VegMenu v = new VegMenu();
        return v;
    }
}

 class VegNonBothRestaurant implements Hotel
{
    public Menus getMenus()
    {
        Both b = new Both();
        return b;
    }
}

public class Client {
}
