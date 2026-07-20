package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.location;

import MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A uniform-grid spatial index. Space is bucketed into {@code cellSize} squares;
 * a driver lives in exactly one cell. A radius search only scans the cells the
 * radius touches — O(drivers near the query), not O(all drivers).
 *
 * Concurrency:
 *   - {@code driverLocations} and {@code cellToDrivers} are concurrent maps, so
 *     location updates and radius searches run without a global lock.
 *   - A move (cell A → cell B) is remove-from-A then add-to-B. A search racing
 *     that move might briefly miss or double-count a driver — harmless, because
 *     the authoritative gate is the atomic {@code tryClaim()} at assignment, not
 *     the candidate list. A driver's own updates are effectively single-threaded.
 */
public class GridLocationService implements LocationService {

    private final double cellSize;
    private final ConcurrentHashMap<String, Location> driverLocations = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Set<String>> cellToDrivers = new ConcurrentHashMap<>();

    public GridLocationService(double cellSize) {
        if (cellSize <= 0) throw new IllegalArgumentException("cellSize must be > 0");
        this.cellSize = cellSize;
    }

    @Override
    public void updateLocation(String driverId, Location location) {
        Location previous = driverLocations.put(driverId, location);
        long newCell = cellKey(location);
        long oldCell = previous == null ? Long.MIN_VALUE : cellKey(previous);
        if (oldCell == newCell) return;                      // still in the same bucket

        if (previous != null) {
            Set<String> old = cellToDrivers.get(oldCell);
            if (old != null) old.remove(driverId);
        }
        cellToDrivers.computeIfAbsent(newCell, k -> ConcurrentHashMap.newKeySet()).add(driverId);
    }

    @Override
    public void remove(String driverId) {
        Location previous = driverLocations.remove(driverId);
        if (previous != null) {
            Set<String> cell = cellToDrivers.get(cellKey(previous));
            if (cell != null) cell.remove(driverId);
        }
    }

    @Override
    public Location locationOf(String driverId) {
        return driverLocations.get(driverId);
    }

    @Override
    public List<String> nearbyDriverIds(Location center, double radius) {
        int minCx = cellIndex(center.x() - radius), maxCx = cellIndex(center.x() + radius);
        int minCy = cellIndex(center.y() - radius), maxCy = cellIndex(center.y() + radius);

        List<String> result = new ArrayList<>();
        for (int cx = minCx; cx <= maxCx; cx++) {
            for (int cy = minCy; cy <= maxCy; cy++) {
                Set<String> cell = cellToDrivers.get(pack(cx, cy));
                if (cell == null) continue;
                for (String driverId : cell) {
                    Location loc = driverLocations.get(driverId);   // re-read: may have moved
                    if (loc != null && loc.distanceTo(center) <= radius) {
                        result.add(driverId);
                    }
                }
            }
        }
        return result;
    }

    // ---- grid math ----
    private int cellIndex(double coord) { return (int) Math.floor(coord / cellSize); }
    private long cellKey(Location loc) { return pack(cellIndex(loc.x()), cellIndex(loc.y())); }
    private static long pack(int cx, int cy) { return (((long) cx) << 32) | (cy & 0xffffffffL); }
}
