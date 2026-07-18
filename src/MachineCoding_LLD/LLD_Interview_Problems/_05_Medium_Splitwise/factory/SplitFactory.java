package MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.factory;

import java.util.EnumMap;
import java.util.Map;

import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.model.SplitType;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy.EqualSplitStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy.ExactSplitStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy.PercentageSplitStrategy;
import MachineCoding_LLD.LLD_Interview_Problems._05_Medium_Splitwise.strategy.SplitStrategy;

/**
 * Maps a {@link SplitType} to its {@link SplitStrategy}. The strategies are stateless, so one shared
 * instance each is cached in an {@link EnumMap} — the service just asks {@code create(type)} without
 * knowing the concrete classes.
 *
 * <p>Pattern reference: {@code DesignPatterns/_01_FactoryDesignPattern}.
 */
public final class SplitFactory {

    private static final Map<SplitType, SplitStrategy> STRATEGIES = new EnumMap<>(SplitType.class);

    static {
        STRATEGIES.put(SplitType.EQUAL, new EqualSplitStrategy());
        STRATEGIES.put(SplitType.EXACT, new ExactSplitStrategy());
        STRATEGIES.put(SplitType.PERCENTAGE, new PercentageSplitStrategy());
    }

    private SplitFactory() {
    }

    public static SplitStrategy create(SplitType type) {
        SplitStrategy strategy = STRATEGIES.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("no split strategy for " + type);
        }
        return strategy;
    }
}
