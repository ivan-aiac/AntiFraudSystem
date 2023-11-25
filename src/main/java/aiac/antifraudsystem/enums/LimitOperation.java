package aiac.antifraudsystem.enums;

import java.util.function.BiFunction;

public enum LimitOperation {
    INCREASE((a,b) -> (long) Math.ceil(0.8 * a + 0.2 * b)),
    DECREASE((a,b) -> (long) Math.ceil(0.8 * a - 0.2 * b));

    private final BiFunction<Long, Long, Long> function;

    LimitOperation(BiFunction<Long, Long, Long> function) {
        this.function = function;
    }

    public long applyFunction(long currentLimit, long transactionAmount) {
        return function.apply(currentLimit, transactionAmount);
    }
}
