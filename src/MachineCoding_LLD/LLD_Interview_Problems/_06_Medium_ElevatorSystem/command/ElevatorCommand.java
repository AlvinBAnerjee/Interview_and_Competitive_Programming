package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.command;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * COMMAND — a stop request turned into an object. {@link Elevator#submit} enqueues these
 * instead of calling any "add a stop" method directly, so a hall/car call can be created by
 * one thread (the dispatcher, or a cabin panel) and {@code execute()}d much later by a
 * completely different thread (that car's own control loop) with no coupling between them.
 */
public interface ElevatorCommand {
    int floor();
    void execute(Elevator elevator);
}
