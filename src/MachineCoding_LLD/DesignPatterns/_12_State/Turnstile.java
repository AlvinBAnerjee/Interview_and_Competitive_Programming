package MachineCoding_LLD.DesignPatterns._12_State;

/**
 * CONTEXT. Holds the current {@link TurnstileState} and forwards each event to it — with no
 * conditional logic of its own. The states call {@link #setState} to transition. One
 * instance of each state is reused because states carry no data.
 */
public class Turnstile {

    private final TurnstileState locked = new LockedState();
    private final TurnstileState unlocked = new UnlockedState();
    private TurnstileState state = locked; // starts locked

    public void coin() {
        state.coin(this);
    }

    public void push() {
        state.push(this);
    }

    void setState(TurnstileState newState) {
        this.state = newState;
    }

    TurnstileState locked() {
        return locked;
    }

    TurnstileState unlocked() {
        return unlocked;
    }

    public TurnstileState state() {
        return state;
    }
}
