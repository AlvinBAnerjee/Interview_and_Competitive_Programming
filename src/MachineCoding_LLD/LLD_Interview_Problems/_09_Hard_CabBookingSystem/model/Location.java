package MachineCoding_LLD.LLD_Interview_Problems._09_Hard_CabBookingSystem.model;

/**
 * A point on a 2-D plane (units, e.g. km). Euclidean distance is used for
 * matching — a real system would use lat/lng + Haversine, but the geometry is
 * orthogonal to the design, so a flat plane keeps the demo readable.
 */
public record Location(double x, double y) {
    public double distanceTo(Location other) {
        double dx = x - other.x, dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
