package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

/**
 * A vehicle wanting to park. Abstract on purpose: the concrete subtypes
 * ({@link Motorcycle}, {@link Car}, {@link Truck}) are created through
 * {@code VehicleFactory}, which keeps {@code new} out of the client code and
 * gives us one place to evolve construction later.
 */
public abstract class Vehicle {

    private final String licensePlate;
    private final VehicleType type;

    protected Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + "(" + licensePlate + ")";
    }
}
