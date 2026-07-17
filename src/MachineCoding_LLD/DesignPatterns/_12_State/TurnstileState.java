package MachineCoding_LLD.DesignPatterns._12_State;

/**
 * STATE — the interface every mode implements. The same two events (`coin`, `push`) do
 * different things depending on which concrete state is current, and each state decides the
 * transition. This replaces `if (locked) … else …` branches sprinkled through the context.
 */
public interface TurnstileState {
    void coin(Turnstile turnstile);
    void push(Turnstile turnstile);
}
