package MachineCoding_LLD.DesignPatterns._12_State;

/** Barrier up. Pushing passes through and re-locks; an extra coin is wasted. */
public class UnlockedState implements TurnstileState {

    @Override
    public void coin(Turnstile turnstile) {
        System.out.println("UNLOCKED + coin → returned (already unlocked)");
    }

    @Override
    public void push(Turnstile turnstile) {
        System.out.println("UNLOCKED + push → pass through, locking");
        turnstile.setState(turnstile.locked());
    }

    @Override
    public String toString() {
        return "UNLOCKED";
    }
}
