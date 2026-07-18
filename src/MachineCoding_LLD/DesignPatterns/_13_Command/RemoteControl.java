package MachineCoding_LLD.DesignPatterns._13_Command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * INVOKER — triggers a {@link Command} without knowing its receiver or what it does.
 * Because the request is an object, the invoker can also keep a history and support
 * {@code undo} for free — logic a plain {@code light.on()} call could never give you.
 */
public class RemoteControl {

    private final Deque<Command> history = new ArrayDeque<>();

    public void pressButton(Command command) {
        command.execute();
        history.push(command);
    }

    public void pressUndo() {
        if (history.isEmpty()) {
            System.out.println("nothing to undo");
            return;
        }
        history.pop().undo();
    }
}
