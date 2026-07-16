package MachineCoding_LLD.DesignPatterns._2_AbstractFactoryDesignPattern;

// Abstract factory: produces a family of related products (Chair + Sofa)
// that are guaranteed to belong to the same style.
public interface FurnitureFactory {
    Chair createChair();
    Sofa createSofa();
}
