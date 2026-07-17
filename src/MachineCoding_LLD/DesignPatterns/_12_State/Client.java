package MachineCoding_LLD.DesignPatterns._12_State;

/**
 * Drives the turnstile through a full cycle. Notice the SAME calls (`coin`, `push`) produce
 * different behavior as the state changes — that's the State pattern.
 */
public class Client {

    public static void main(String[] args) {
        Turnstile turnstile = new Turnstile();
        System.out.println("start: " + turnstile.state());

        turnstile.push(); // denied — still locked
        turnstile.coin(); // unlocks
        turnstile.coin(); // wasted — already unlocked
        turnstile.push(); // pass through, re-locks
        turnstile.push(); // denied again

        System.out.println("end:   " + turnstile.state());
    }
}
