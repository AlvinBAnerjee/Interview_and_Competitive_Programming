package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A read-only snapshot of a show's seat availability, returned by
 * {@code getAvailability}. Purely a view object — it holds no live state and is
 * safe to hand to a caller/UI.
 */
public class SeatMap {
    private final String showId;
    private final List<Seat> seats;                 // layout order
    private final Map<String, SeatStatus> statuses; // seatId -> status at snapshot time

    public SeatMap(String showId, List<Seat> seats, Map<String, SeatStatus> statuses) {
        this.showId = showId;
        this.seats = seats;
        this.statuses = statuses;
    }

    public String showId() { return showId; }
    public SeatStatus statusOf(String seatId) { return statuses.get(seatId); }
    public Map<String, SeatStatus> statuses() { return statuses; }

    /** ASCII grid: '.' available, 'H' held, 'X' booked — grouped by row. */
    public String render() {
        StringBuilder sb = new StringBuilder("Show ").append(showId)
                .append("   [ . available   H held   X booked ]\n");
        // group seats by row -> col
        TreeMap<Integer, TreeMap<Integer, Seat>> grid = new TreeMap<>();
        for (Seat s : seats) {
            grid.computeIfAbsent(s.row(), r -> new TreeMap<>()).put(s.col(), s);
        }
        for (var rowEntry : grid.entrySet()) {
            sb.append(String.format("  R%d ", rowEntry.getKey()));
            for (Seat s : rowEntry.getValue().values()) {
                sb.append(glyphFor(statuses.get(s.id()))).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private static char glyphFor(SeatStatus status) {
        if (status == null) return '?';
        return switch (status) {
            case AVAILABLE -> '.';
            case HELD -> 'H';
            case BOOKED -> 'X';
        };
    }
}
