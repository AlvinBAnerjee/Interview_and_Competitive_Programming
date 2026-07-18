package MachineCoding_LLD.DesignPatterns._13_Command;

/**
 * COMMAND — turns a request into a stand-alone object. The invoker holds only this
 * interface, so it can execute, queue, log, or undo requests without knowing which
 * receiver they act on or how.
 */
public interface Command {
    void execute();
    void undo();
}
