package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.model;

import java.util.List;

/** A theatre in a city, owning one or more screens. */
public class Theatre {
    private final String id;
    private final String name;
    private final City city;
    private final List<Screen> screens;

    public Theatre(String id, String name, City city, List<Screen> screens) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.screens = List.copyOf(screens);
    }

    public String id() { return id; }
    public String name() { return name; }
    public City city() { return city; }
    public List<Screen> screens() { return screens; }
}
