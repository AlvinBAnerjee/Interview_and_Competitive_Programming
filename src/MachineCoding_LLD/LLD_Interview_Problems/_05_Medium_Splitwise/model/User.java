package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model;

/** A person. Identity is the {@code id}; {@code name} is display only. */
public record User(String id, String name) {
    public User {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("user id must not be blank");
        }
    }
}
