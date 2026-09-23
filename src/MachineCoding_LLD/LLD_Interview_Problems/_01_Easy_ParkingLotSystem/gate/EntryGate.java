package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.gate;

import java.util.Optional;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.ParkingLot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Vehicle;

/**
 * A physical entry gate. It only forwards the request to the lot.
 *
 * Why it exists at all: several gates share ONE ParkingLot and each runs on its own
 * thread. That is exactly the situation slot allocation has to survive.
 */
public class EntryGate {

    private final int id;
    private final ParkingLot lot;

    public EntryGate(int id, ParkingLot lot) {
        this.id = id;
        this.lot = lot;
    }

    /** Empty means the lot is full for this vehicle type. */
    public Optional<Ticket> park(Vehicle vehicle) {
        return lot.park(vehicle);
    }

    public int getId() {
        return id;
    }
}
