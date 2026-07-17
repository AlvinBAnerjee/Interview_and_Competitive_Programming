package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;

/**
 * Per-hour, per-vehicle-type pricing with a 1-hour minimum and rounding up to the next
 * whole hour (the usual real-world scheme).
 */
public class HourlyPricingStrategy implements PricingStrategy {

    private final Map<VehicleType, Double> hourlyRate;

    public HourlyPricingStrategy(Map<VehicleType, Double> hourlyRate) {
        this.hourlyRate = new EnumMap<>(hourlyRate);
    }

    /** Sensible defaults: bikes cheapest, trucks dearest. */
    public static HourlyPricingStrategy withDefaults() {
        Map<VehicleType, Double> rates = new EnumMap<>(VehicleType.class);
        rates.put(VehicleType.MOTORCYCLE, 10.0);
        rates.put(VehicleType.CAR, 20.0);
        rates.put(VehicleType.TRUCK, 40.0);
        return new HourlyPricingStrategy(rates);
    }

    @Override
    public double calculateFare(Ticket ticket, Instant exitTime) {
        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();
        long hours = Math.max(1, (long) Math.ceil(minutes / 60.0)); // min 1h, round up
        return hours * hourlyRate.get(ticket.getVehicle().getType());
    }
}
