package MachineCoding_LLD.DesignPatterns._02_AbstractFactoryDesignPattern;

public class ModernChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Sitting on a modern chair");
    }
}
