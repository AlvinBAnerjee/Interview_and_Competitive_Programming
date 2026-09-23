package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.factory.VehicleFactory;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.gate.EntryGate;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.gate.ExitGate;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.ParkingFloor;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Vehicle;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy.HourlyPricingStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy.NearestSlotStrategy;

/**
 * A walkthrough of the happy path: build a lot, park a few vehicles from two gates,
 * exit one, and watch the freed slot get reused.
 *
 * For the correctness and concurrency checks, run ParkingLotTest instead.
 */
public class Main {

    public static void main(String[] args) {

        // ---- 1. Build the lot: 2 floors, each with 2 bike + 3 car + 1 truck slots ----
        Map<VehicleType, Integer> slotsPerFloor = new EnumMap<>(VehicleType.class);
        slotsPerFloor.put(VehicleType.MOTORCYCLE, 2);
        slotsPerFloor.put(VehicleType.CAR, 3);
        slotsPerFloor.put(VehicleType.TRUCK, 1);

        List<ParkingFloor> floors = ParkingFloor.createFloors(2, slotsPerFloor);

        // ---- 2. Pick the two policies, then create the one shared lot ----
        NearestSlotStrategy slotStrategy = new NearestSlotStrategy(floors);
        HourlyPricingStrategy pricingStrategy = HourlyPricingStrategy.withDefaults();

        ParkingLot lot = ParkingLot.configure(floors, slotStrategy, pricingStrategy);

        // ---- 3. Two ways in, one way out ----
        EntryGate gateA = new EntryGate(1, lot);
        EntryGate gateB = new EntryGate(2, lot);
        ExitGate exitGate = new ExitGate(1, lot);

        System.out.println("Car slots free at start: " + lot.availableSlots(VehicleType.CAR));

        // ---- 4. Two cars come in through different gates ----
        // They get the two NEAREST car slots (F0-CAR-0 and F0-CAR-1), never the same one.
        Ticket ticketA = gateA.park(createCar("KA-01-AAA")).orElseThrow();
        Ticket ticketB = gateB.park(createCar("KA-02-BBB")).orElseThrow();

        System.out.println("Gate A parked at: " + ticketA.getSlot());
        System.out.println("Gate B parked at: " + ticketB.getSlot());
        System.out.println("Car slots free now: " + lot.availableSlots(VehicleType.CAR));

        // ---- 5. A motorcycle uses its own set of slots ----
        Vehicle motorcycle = VehicleFactory.create(VehicleType.MOTORCYCLE, "KA-03-CCC");
        Ticket ticketC = gateA.park(motorcycle).orElseThrow();
        System.out.println("Motorcycle parked at: " + ticketC.getSlot());

        // ---- 6. The first car leaves and pays (minimum 1 hour for a car = 20.0) ----
        double fare = exitGate.unpark(ticketA);
        System.out.println("Car " + ticketA.getVehicle().getLicensePlate()
                + " left, fare = " + fare);
        System.out.println("Car slots free after exit: " + lot.availableSlots(VehicleType.CAR));

        // ---- 7. The next car gets that same freed slot back, because it is nearest ----
        Optional<Ticket> ticketD = gateB.park(createCar("KA-04-DDD"));
        System.out.println("Next car reused slot: " + ticketD.orElseThrow().getSlot());
    }

    private static Vehicle createCar(String licensePlate) {
        return VehicleFactory.create(VehicleType.CAR, licensePlate);
    }
}
