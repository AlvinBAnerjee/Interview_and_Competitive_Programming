package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

import java.util.List;

/** A screen/auditorium: a fixed physical seat layout inside a theatre. */
public class Screen {
    private final String id;
    private final String name;
    private final List<Seat> seats;   // the physical layout, shared by all shows on this screen

    public Screen(String id, String name, List<Seat> seats) {
        this.id = id;
        this.name = name;
        this.seats = List.copyOf(seats);
    }

    public String id() { return id; }
    public String name() { return name; }
    public List<Seat> seats() { return seats; }
}
