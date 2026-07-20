package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model;

import java.util.EnumSet;
import java.util.Set;

/**
 * Ride lifecycle as an enum-encoded state machine: each state declares which
 * states it may legally move to, and {@link Ride#transitionTo} enforces it.
 *
 * This is the lightweight face of the State pattern — right when states differ
 * only in *which transitions are legal*. When each state needs materially
 * different *behaviour*, promote to one class per state (see the Elevator
 * solution's {@code state/} package). Called out in SOLUTION.md §5.
 */
public enum RideState {
    REQUESTED,
    DRIVER_ASSIGNED,
    STARTED,
    COMPLETED,
    CANCELLED;

    private Set<RideState> allowedNext;

    static {
        REQUESTED.allowedNext       = EnumSet.of(DRIVER_ASSIGNED, CANCELLED);
        DRIVER_ASSIGNED.allowedNext = EnumSet.of(STARTED, CANCELLED);
        STARTED.allowedNext         = EnumSet.of(COMPLETED, CANCELLED);
        COMPLETED.allowedNext       = EnumSet.noneOf(RideState.class);  // terminal
        CANCELLED.allowedNext       = EnumSet.noneOf(RideState.class);  // terminal
    }

    public boolean canTransitionTo(RideState next) {
        return allowedNext.contains(next);
    }

    public boolean isTerminal() {
        return allowedNext.isEmpty();
    }
}
