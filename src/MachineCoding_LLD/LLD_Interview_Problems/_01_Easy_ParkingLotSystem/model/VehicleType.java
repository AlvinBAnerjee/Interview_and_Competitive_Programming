package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

/**
 * The kinds of vehicles the lot supports. A {@link ParkingSlot} serves exactly one
 * type, and a vehicle can only take a slot of its own type. Keeping this an enum
 * (rather than free-form strings) makes the slot/pricing maps type-safe and lets us
 * size per-type structures with {@link java.util.EnumMap}.
 */
public enum VehicleType {
    MOTORCYCLE,
    CAR,
    TRUCK
}
