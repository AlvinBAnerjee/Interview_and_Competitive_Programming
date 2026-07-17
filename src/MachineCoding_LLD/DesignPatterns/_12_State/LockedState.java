package MachineCoding_LLD.DesignPatterns._12_State;

/** Barrier down. A coin unlocks it; pushing does nothing. */
public class LockedState implements TurnstileState {

    @Override
    public void coin(Turnstile turnstile) {
        System.out.println("LOCKED + coin  → unlocking");
        turnstile.setState(turnstile.unlocked());
    }

    @Override
    public void push(Turnstile turnstile) {
        System.out.println("LOCKED + push  → denied (clunk)");
    }

    @Override
    public String toString() {
        return "LOCKED";
    }
}
