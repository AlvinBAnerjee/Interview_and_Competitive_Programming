package MachineCoding_LLD.DesignPatterns._13_Command;

/**
 * CONCRETE COMMAND — the inverse of {@link LightOnCommand}; {@code undo()} is just the
 * other action, which is why Command makes undo/redo stacks so natural.
 */
public class LightOffCommand implements Command {

    private final Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.off();
    }

    @Override
    public void undo() {
        light.on();
    }
}
