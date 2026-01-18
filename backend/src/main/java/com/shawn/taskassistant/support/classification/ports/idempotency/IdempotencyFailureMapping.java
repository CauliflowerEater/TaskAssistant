package com.shawn.taskassistant.support.classification.ports.idempotency;

import com.shawn.taskassistant.support.failure.FailureSemantic;
import com.shawn.taskassistant.port.idempotency.errors.IdempotencyPortFailureReason;

import java.util.EnumMap;
import java.util.Map;

/**
 * Mapping definition only:
 * IdempotencyPortFailureReason -> System FailureSemantic
 *
 */
public final class IdempotencyFailureMapping {

    public static final Map<IdempotencyPortFailureReason, FailureSemantic> TABLE;

    static {
        EnumMap<IdempotencyPortFailureReason, FailureSemantic> m =
            new EnumMap<>(IdempotencyPortFailureReason.class);

        m.put(IdempotencyPortFailureReason.INVALID_INPUT, FailureSemantic.INPUT_INVALID);
        m.put(IdempotencyPortFailureReason.STORAGE_UNAVAILABLE, FailureSemantic.DEPENDENCY_UNAVAILABLE);
        m.put(IdempotencyPortFailureReason.STORAGE_TIMEOUT, FailureSemantic.DEPENDENCY_TIMEOUT);
        m.put(IdempotencyPortFailureReason.DATA_CORRUPTION, FailureSemantic.INVARIANT_VIOLATION);
        m.put(IdempotencyPortFailureReason.INTERNAL_ERROR, FailureSemantic.INVARIANT_VIOLATION);

        for (IdempotencyPortFailureReason r : IdempotencyPortFailureReason.values()) {
            if (!m.containsKey(r)) {
                throw new IllegalStateException(
                    "Missing IdempotencyFailureMapping entry for reason=" + r
                );
            }
        }

        TABLE = Map.copyOf(m);
    }

    private IdempotencyFailureMapping() {}
}