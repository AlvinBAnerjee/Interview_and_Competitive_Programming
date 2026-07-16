package MachineCoding_LLD.DesignPatterns._02_AbstractFactoryDesignPattern;

public class VictorianChair implements Chair {
    @Override
    public void sitOn() {
        System.out.println("Sitting on a victorian chair");
    }
}
