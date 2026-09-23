package MachineCoding_LLD.DesignPatterns._06_Builder;

/**
 * Interview-style "fluent" Builder: one class, chained setters, a single
 * build() at the end. There is no separate Director here — the caller plays
 * that role, invoking whichever steps it wants, in whatever order, then
 * calling build(). This is the shape almost everyone means when they say
 * "the Builder pattern" in a coding interview, as opposed to the classic
 * GoF Director/ConcreteBuilder split in Builder.java.
 *
 * Trade-off vs the classic form: you lose a reusable, enforced construction
 * *sequence* (there's no Director to guarantee "basement before roof"), but
 * you gain the ability to set only the fields you care about, in any order —
 * which is exactly what you want when most fields are optional and there's
 * only one kind of object being built (no Igloo vs Tipi family here).
 */
class FluentHouse {

    private final String basement;
    private final String structure;
    private final String roof;
    private final String interior;

    private FluentHouse(Builder builder) {
        this.basement = builder.basement;
        this.structure = builder.structure;
        this.roof = builder.roof;
        this.interior = builder.interior;
    }

    @Override
    public String toString() {
        return "FluentHouse{basement=" + basement + ", structure=" + structure
                + ", roof=" + roof + ", interior=" + interior + "}";
    }

    // Nested inside the product it builds — a common convention that keeps
    // the Builder physically next to the class it constructs.
    static class Builder {
        private String basement = "none";
        private String structure = "none";
        private String roof = "none";
        private String interior = "none";

        Builder basement(String basement) {
            this.basement = basement;
            return this;                 // <-- the whole trick: return this
        }

        Builder structure(String structure) {
            this.structure = structure;
            return this;
        }

        Builder roof(String roof) {
            this.roof = roof;
            return this;
        }

        Builder interior(String interior) {
            this.interior = interior;
            return this;
        }

        FluentHouse build() {
            return new FluentHouse(this);
        }
    }
}

public class FluentHouseBuilderDemo {
    public static void main(String[] args) {
        // Only three of the four fields are set, in a different order than
        // they're declared -- the classic Director-driven form can't do
        // either of those without the Director itself changing.
        FluentHouse house = new FluentHouse.Builder()
                .roof("Shingles")
                .basement("Concrete")
                .structure("Brick")
                .build();

        System.out.println("Fluent builder constructed: " + house);
    }
}
