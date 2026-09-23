package MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.strategy;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.Ticket;
import MachineCoding_LLD.LLD_Interview_Problems._01_Easy_ParkingLotSystem.model.VehicleType;

/**
 * Charges per hour, at a different rate per vehicle type. You always pay for at least
 * one hour, and part-hours round up -- the usual real-world car-park rule.
 *
 * Example: a car staying 2h30m pays ceil(2.5) = 3 hours * 20.0 = 60.0
 */
public class HourlyPricingStrategy implements PricingStrategy {

    private final Map<VehicleType, Double> hourlyRate;

    public HourlyPricingStrategy(Map<VehicleType, Double> hourlyRate) {
        this.hourlyRate = new EnumMap<>(hourlyRate);
    }

    /** Bikes cheapest, trucks dearest. */
    public static HourlyPricingStrategy withDefaults() {
        Map<VehicleType, Double> rates = new EnumMap<>(VehicleType.class);
        rates.put(VehicleType.MOTORCYCLE, 10.0);
        rates.put(VehicleType.CAR, 20.0);
        rates.put(VehicleType.TRUCK, 40.0);
        return new HourlyPricingStrategy(rates);
    }

    @Override
    public double calculateFare(Ticket ticket, Instant exitTime) {
        long minutesParked = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();

        long hoursCharged = (long) Math.ceil(minutesParked / 60.0);
        if (hoursCharged < 1) {
            hoursCharged = 1;                       // minimum one hour
        }

        double ratePerHour = hourlyRate.get(ticket.getVehicle().getType());
        return hoursCharged * ratePerHour;
    }
}
