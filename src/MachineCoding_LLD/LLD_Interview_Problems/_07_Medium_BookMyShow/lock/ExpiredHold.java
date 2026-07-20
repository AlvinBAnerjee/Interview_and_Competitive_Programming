package MachineCoding_LLD.LLD_Interview_Problems._07_Medium_BookMyShow.lock;

/** One hold that lapsed, surfaced by the reaper so observers can notify the user. */
public record ExpiredHold(String showId, String seatId, String userId) { }
