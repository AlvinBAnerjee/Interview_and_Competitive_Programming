package MachineCoding_LLD.DesignPatterns._03_SingletonDesignPattern;

public class Singleton {

    private Singleton() {}

    // Holder class is loaded only on first getInstance() call, and class-loading
    // is already thread-safe by JVM guarantee, so no synchronization is needed.
    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }

    public static void main(String[] args) {
        Singleton first = Singleton.getInstance();
        Singleton second = Singleton.getInstance();
        System.out.println("Same instance: " + (first == second));
    }
}
