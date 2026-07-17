package MachineCoding_LLD.LLD_Interview_Problems._02_Easy_VendingMachine.model;

/**
 * A product is immutable reference data: a slot code (what the user keys in), a display
 * name, and a price. It's a record, not a class hierarchy — there's no polymorphic
 * behavior here, so a Factory of Product subtypes would be ceremony with no payoff.
 */
public record Product(String code, String name, int price) {
}
