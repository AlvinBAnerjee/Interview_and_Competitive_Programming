package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model;

/**
 * A vehicle that wants to park. The concrete subtypes (Motorcycle, Car, Truck) are
 * created through VehicleFactory, so callers never write `new Car(...)`.
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
