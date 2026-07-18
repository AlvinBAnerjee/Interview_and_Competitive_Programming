package MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.state;

import MachineCoding_LLD.LLD_Interview_Problems._06_Medium_ElevatorSystem.Elevator;

/**
 * STATE — one step of the car's control loop. {@link Elevator#run} calls {@code next}
 * repeatedly, each call doing (and possibly blocking for) one unit of work — waiting for a
 * request, moving one floor, or holding the doors open — and returning whichever state
 * should run next. No {@code switch (status)} anywhere in {@link Elevator} itself.
 */
public interface ElevatorState {
    ElevatorState next(Elevator elevator) throws InterruptedException;
}
