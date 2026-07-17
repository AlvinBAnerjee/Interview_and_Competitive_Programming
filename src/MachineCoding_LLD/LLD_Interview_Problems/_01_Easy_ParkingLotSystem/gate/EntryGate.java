package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.gate;

import java.util.Optional;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.ParkingLot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Vehicle;

/**
 * A physical entry gate. Deliberately thin — it just forwards to the lot. Its value is
 * conceptual: multiple gates share one {@link ParkingLot}, which is exactly the scenario
 * the thread-safe allocation must survive (two cars, two gates, one nearest slot).
 */
public class EntryGate {

    private final int id;
    private final ParkingLot lot;

    public EntryGate(int id, ParkingLot lot) {
        this.id = id;
        this.lot = lot;
    }

    public Optional<Ticket> park(Vehicle vehicle) {
        return lot.park(vehicle);
    }

    public int getId() {
        return id;
    }
}
