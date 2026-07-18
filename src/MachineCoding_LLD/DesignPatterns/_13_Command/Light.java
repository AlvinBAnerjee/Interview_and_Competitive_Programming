package MachineCoding_LLD.DesignPatterns._13_Command;

/**
 * RECEIVER — knows how to actually perform the work. It has no idea a Command exists.
 */
public class Light {

    private final String room;
    private boolean on;

    public Light(String room) {
        this.room = room;
    }

    public void on() {
        on = true;
        System.out.println(room + " light: ON");
    }

    public void off() {
        on = false;
        System.out.println(room + " light: OFF");
    }

    public boolean isOn() {
        return on;
    }
}
