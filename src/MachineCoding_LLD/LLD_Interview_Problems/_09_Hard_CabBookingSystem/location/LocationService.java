package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.location;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Location;

import java.util.List;

/**
 * Spatial index over driver locations. Kept behind an interface so the grid
 * implementation can be swapped for a geohash / quadtree / Redis GEO without
 * touching the matching code.
 */
public interface LocationService {
    /** Upsert a driver's live location. Concurrent with searches. */
    void updateLocation(String driverId, Location location);

    /** Remove a driver (goes offline). */
    void remove(String driverId);

    /** Current location of a driver, or null if unknown. */
    Location locationOf(String driverId);

    /** Ids of drivers within {@code radius} of {@code center} — better than O(N). */
    List<String> nearbyDriverIds(Location center, double radius);
}
