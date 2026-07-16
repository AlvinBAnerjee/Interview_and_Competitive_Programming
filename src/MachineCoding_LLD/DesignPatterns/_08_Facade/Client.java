package MachineCoding_LLD.DesignPatterns._08_Facade;
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

// Facade: hides the complexity of picking the right restaurant subsystem
// for a diner's preference behind a single simple call.
class RestaurantFacade
{
    private final Hotel vegRestaurant = new VegRestaurant();
    private final Hotel nonVegRestaurant = new NonVegRestaurant();
    private final Hotel mixedRestaurant = new VegNonBothRestaurant();

    public Menus getMenuFor(String preference)
    {
        switch (preference.toLowerCase())
        {
            case "veg":
                return vegRestaurant.getMenus();
            case "nonveg":
                return nonVegRestaurant.getMenus();
            default:
                return mixedRestaurant.getMenus();
        }
    }
}

public class Client {
    public static void main(String[] args) {
        RestaurantFacade facade = new RestaurantFacade();

        System.out.println("Got menu: " + facade.getMenuFor("veg").getClass().getSimpleName());
        System.out.println("Got menu: " + facade.getMenuFor("nonveg").getClass().getSimpleName());
        System.out.println("Got menu: " + facade.getMenuFor("both").getClass().getSimpleName());
    }
}
