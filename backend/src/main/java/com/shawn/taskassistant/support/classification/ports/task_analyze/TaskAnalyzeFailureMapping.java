package com.shawn.taskassistant.support.classification.ports.task_analyze;

import com.shawn.taskassistant.port.taskAnalyzeLLM.errors.TaskAnalyzeLLMPortFailureReason;
import com.shawn.taskassistant.support.failure.FailureSemantic;

import java.util.EnumMap;
import java.util.Map;

/**
 * Mapping definition only:
 * StructuredGenerationPortFailureReason -> System FailureSemantic
 *
 */
public final class TaskAnalyzeFailureMapping {

    public static final Map<TaskAnalyzeLLMPortFailureReason, FailureSemantic> TABLE;

    static {
        EnumMap<TaskAnalyzeLLMPortFailureReason, FailureSemantic> m =
            new EnumMap<>(TaskAnalyzeLLMPortFailureReason.class);

        m.put(TaskAnalyzeLLMPortFailureReason.INVALID_INPUT, FailureSemantic.INPUT_INVALID);
        m.put(TaskAnalyzeLLMPortFailureReason.SCHEMA_NOT_FOUND, FailureSemantic.DEPENDENCY_BAD_REQUEST);
        m.put(TaskAnalyzeLLMPortFailureReason.STRUCTURED_OUTPUT_VIOLATION, FailureSemantic.STRUCTURED_OUTPUT_INVALID);
        m.put(TaskAnalyzeLLMPortFailureReason.TIMEOUT, FailureSemantic.DEPENDENCY_TIMEOUT);
        m.put(TaskAnalyzeLLMPortFailureReason.RATE_LIMITED, FailureSemantic.DEPENDENCY_RATE_LIMITED);
        m.put(TaskAnalyzeLLMPortFailureReason.DEPENDENCY_FAILURE, FailureSemantic.DEPENDENCY_UNAVAILABLE);
        m.put(TaskAnalyzeLLMPortFailureReason.INTERNAL_SDK_ERROR, FailureSemantic.INVARIANT_VIOLATION);

        TABLE = Map.copyOf(m);
    }

    private TaskAnalyzeFailureMapping() {}
}
