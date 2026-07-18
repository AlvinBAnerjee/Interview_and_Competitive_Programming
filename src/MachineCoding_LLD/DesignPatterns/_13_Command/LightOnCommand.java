package MachineCoding_LLD.DesignPatterns._13_Command;

/**
 * CONCRETE COMMAND — binds one receiver ({@link Light}) to one action at creation time.
 */
public class LightOnCommand implements Command {

    private final Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.on();
    }

    @Override
    public void undo() {
        light.off();
    }
}
