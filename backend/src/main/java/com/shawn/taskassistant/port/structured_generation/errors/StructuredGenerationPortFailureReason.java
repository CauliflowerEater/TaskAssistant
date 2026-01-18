package com.shawn.taskassistant.port.structured_generation.errors;

public enum StructuredGenerationPortFailureReason {
    INVALID_INPUT,
    SCHEMA_NOT_FOUND,
    STRUCTURED_OUTPUT_VIOLATION,
    TIMEOUT,
    RATE_LIMITED,
    DEPENDENCY_FAILURE,
    INTERNAL_SDK_ERROR
}

