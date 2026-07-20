package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A screening of one movie on one screen at a given time. Owns the per-show
 * seat state ({@link ShowSeat} per physical seat) and the base price the
 * pricing strategy scales by seat category.
 */
public class Show {
    private final String id;
    private final Movie movie;
    private final Theatre theatre;
    private final Screen screen;
    private final LocalDateTime startTime;
    private final long basePricePaise;                       // base ticket price; scaled per category
    private final Map<String, ShowSeat> showSeats;           // seatId -> per-show state (layout order)

    public Show(String id, Movie movie, Theatre theatre, Screen screen,
                LocalDateTime startTime, long basePricePaise) {
        this.id = id;
        this.movie = movie;
        this.theatre = theatre;
        this.screen = screen;
        this.startTime = startTime;
        this.basePricePaise = basePricePaise;
        this.showSeats = new LinkedHashMap<>();
        for (Seat seat : screen.seats()) {
            showSeats.put(seat.id(), new ShowSeat(seat));     // one ShowSeat per physical seat
        }
    }

    public String id() { return id; }
    public Movie movie() { return movie; }
    public Theatre theatre() { return theatre; }
    public Screen screen() { return screen; }
    public LocalDateTime startTime() { return startTime; }
    public long basePricePaise() { return basePricePaise; }

    /** @return the per-show state for a seat, or null if the id isn't on this screen. */
    public ShowSeat showSeat(String seatId) { return showSeats.get(seatId); }

    public Collection<ShowSeat> showSeats() { return showSeats.values(); }
}
