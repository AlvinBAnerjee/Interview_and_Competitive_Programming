package MachineCoding_LLD.DesignPatterns._2_AbstractFactoryDesignPattern;

// Client: works only against the abstract factory/products, so swapping the
// concrete factory changes the whole furniture family consistently.
public class FurnitureShop {

    private final Chair chair;
    private final Sofa sofa;

    public FurnitureShop(FurnitureFactory factory) {
        this.chair = factory.createChair();
        this.sofa = factory.createSofa();
    }

    public void furnish() {
        chair.sitOn();
        sofa.lieOn();
    }

    public static void main(String[] args) {
        FurnitureShop modernShop = new FurnitureShop(new ModernFurnitureFactory());
        modernShop.furnish();

        FurnitureShop victorianShop = new FurnitureShop(new VictorianFurnitureFactory());
        victorianShop.furnish();
    }
}
