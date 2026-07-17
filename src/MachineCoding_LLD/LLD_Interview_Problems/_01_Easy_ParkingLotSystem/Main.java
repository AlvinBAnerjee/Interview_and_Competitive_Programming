package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem;

import java.util.EnumMap;
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
 * A quick end-to-end walkthrough of the happy path across two gates. For the correctness
 * and concurrency checks, see {@code ParkingLotTest}.
 */
public class Main {

    public static void main(String[] args) {
        // 2 floors; each floor has 2 motorcycle, 3 car, 1 truck slots.
        Map<VehicleType, Integer> perType = new EnumMap<>(VehicleType.class);
        perType.put(VehicleType.MOTORCYCLE, 2);
        perType.put(VehicleType.CAR, 3);
        perType.put(VehicleType.TRUCK, 1);
        var floors = ParkingLots.buildFloors(2, perType);

        // Wire the singleton with the nearest-slot + hourly-pricing strategies.
        ParkingLot lot = ParkingLot.configure(
                floors,
                new NearestSlotStrategy(floors),
                HourlyPricingStrategy.withDefaults());

        EntryGate gateA = new EntryGate(1, lot);
        EntryGate gateB = new EntryGate(2, lot);
        ExitGate exit = new ExitGate(1, lot);

        System.out.println("Cars available at start: " + lot.availableSlots(VehicleType.CAR));

        // Two cars enter from two different gates — note they get the two NEAREST slots
        // (F0-CAR-0, F0-CAR-1), never the same one.
        Ticket t1 = gateA.park(car("KA-01-AAA")).orElseThrow();
        Ticket t2 = gateB.park(car("KA-02-BBB")).orElseThrow();
        System.out.println("Gate A parked at " + t1.getSlot());
        System.out.println("Gate B parked at " + t2.getSlot());
        System.out.println("Cars available now:   " + lot.availableSlots(VehicleType.CAR));

        // A motorcycle and a truck too.
        Ticket t3 = gateA.park(VehicleFactory.create(VehicleType.MOTORCYCLE, "KA-03-CCC")).orElseThrow();
        System.out.println("Motorcycle parked at " + t3.getSlot());

        // Exit the first car — fare is at least the 1-hour minimum for a car (20.0).
        double fare = exit.unpark(t1);
        System.out.println("Car " + t1.getVehicle().getLicensePlate() + " exits, fare = " + fare);
        System.out.println("Cars available after exit: " + lot.availableSlots(VehicleType.CAR));

        // The freed nearest slot is reused by the next car.
        Optional<Ticket> t4 = gateB.park(car("KA-04-DDD"));
        System.out.println("Next car reused slot: " + t4.orElseThrow().getSlot());
    }

    private static Vehicle car(String plate) {
        return VehicleFactory.create(VehicleType.CAR, plate);
    }
}
