package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.factory;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Car;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Motorcycle;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Truck;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Vehicle;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;

/**
 * FACTORY pattern. Maps a {@link VehicleType} to the right concrete {@link Vehicle},
 * so callers (entry gates, tests) never do {@code new Car(...)} themselves. Add a new
 * vehicle type in exactly one place here.
 */
public final class VehicleFactory {

    private VehicleFactory() {
    }

    public static Vehicle create(VehicleType type, String licensePlate) {
        return switch (type) {
            case MOTORCYCLE -> new Motorcycle(licensePlate);
            case CAR        -> new Car(licensePlate);
            case TRUCK      -> new Truck(licensePlate);
        };
    }
}
