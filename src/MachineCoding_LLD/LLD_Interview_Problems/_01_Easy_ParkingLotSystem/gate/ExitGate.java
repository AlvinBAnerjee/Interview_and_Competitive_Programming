package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.gate;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.ParkingLot;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;

/**
 * A physical exit gate. Redeems a ticket and returns the fare owed.
 */
public class ExitGate {

    private final int id;
    private final ParkingLot lot;

    public ExitGate(int id, ParkingLot lot) {
        this.id = id;
        this.lot = lot;
    }

    public double unpark(Ticket ticket) {
        return lot.unpark(ticket);
    }

    public int getId() {
        return id;
    }
}
