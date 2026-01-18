package com.shawn.taskassistant.support.classification.ports.structured_generation;

import com.shawn.taskassistant.port.structured_generation.errors.StructuredGenerationPortFailureReason;
import com.shawn.taskassistant.support.failure.FailureSemantic;

import java.util.EnumMap;
import java.util.Map;

/**
 * Mapping definition only:
 * StructuredGenerationPortFailureReason -> System FailureSemantic
 *
 */
public final class StructuredGenerationFailureMapping {

    public static final Map<StructuredGenerationPortFailureReason, FailureSemantic> TABLE;

    static {
        EnumMap<StructuredGenerationPortFailureReason, FailureSemantic> m =
            new EnumMap<>(StructuredGenerationPortFailureReason.class);

        m.put(StructuredGenerationPortFailureReason.INVALID_INPUT, FailureSemantic.INPUT_INVALID);
        m.put(StructuredGenerationPortFailureReason.SCHEMA_NOT_FOUND, FailureSemantic.DEPENDENCY_BAD_REQUEST);
        m.put(StructuredGenerationPortFailureReason.STRUCTURED_OUTPUT_VIOLATION, FailureSemantic.STRUCTURED_OUTPUT_INVALID);
        m.put(StructuredGenerationPortFailureReason.TIMEOUT, FailureSemantic.DEPENDENCY_TIMEOUT);
        m.put(StructuredGenerationPortFailureReason.RATE_LIMITED, FailureSemantic.DEPENDENCY_RATE_LIMITED);
        m.put(StructuredGenerationPortFailureReason.DEPENDENCY_FAILURE, FailureSemantic.DEPENDENCY_UNAVAILABLE);
        m.put(StructuredGenerationPortFailureReason.INTERNAL_SDK_ERROR, FailureSemantic.INVARIANT_VIOLATION);

        TABLE = Map.copyOf(m);
    }

    private StructuredGenerationFailureMapping() {}
}
