package MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.command;

import MachineCoding_LLD.LLD_Interview_Problems._10_Hard_StockExchange.engine.MatchingEngine;

/**
 * An action to run against a book (place / cancel / snapshot), built by a client
 * thread and executed later by the engine's single matcher thread (Command
 * pattern). Turning every book operation into a queued command is exactly what
 * makes the single-writer model work: the queue serializes all access.
 */
public interface EngineCommand {
    void execute(MatchingEngine engine);
}
