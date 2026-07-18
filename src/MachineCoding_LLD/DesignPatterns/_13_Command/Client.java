package MachineCoding_LLD.DesignPatterns._13_Command;

/**
 * Wires receivers to commands, hands them to the invoker, and shows undo unwinding
 * the last action — the same command objects that were executed are replayed backwards.
 */
public class Client {

    public static void main(String[] args) {
        Light kitchen = new Light("Kitchen");
        Light bedroom = new Light("Bedroom");

        RemoteControl remote = new RemoteControl();

        remote.pressButton(new LightOnCommand(kitchen));
        remote.pressButton(new LightOnCommand(bedroom));
        remote.pressButton(new LightOffCommand(kitchen));

        System.out.println("kitchen on? " + kitchen.isOn() + ", bedroom on? " + bedroom.isOn());

        remote.pressUndo(); // undoes "kitchen off" -> kitchen back on
        System.out.println("after undo, kitchen on? " + kitchen.isOn());
    }
}
