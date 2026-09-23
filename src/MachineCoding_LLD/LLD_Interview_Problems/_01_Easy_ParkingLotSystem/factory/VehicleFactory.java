package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.factory;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Car;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Motorcycle;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Truck;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Vehicle;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;

/**
 * FACTORY pattern. Turns a VehicleType into the matching Vehicle subclass, so callers
 * never write `new Car(...)`. Adding a vehicle type is a one-line change here.
 */
public final class VehicleFactory {

    private VehicleFactory() {
    }

    public static Vehicle create(VehicleType type, String licensePlate) {
        switch (type) {
            case MOTORCYCLE:
                return new Motorcycle(licensePlate);
            case CAR:
                return new Car(licensePlate);
            case TRUCK:
                return new Truck(licensePlate);
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
}
