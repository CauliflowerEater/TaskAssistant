package com.shawn.taskassistant.port.taskAnalyzeLLM.errors;

public enum TaskAnalyzeLLMPortFailureReason {
    INVALID_INPUT,
    SCHEMA_NOT_FOUND,
    STRUCTURED_OUTPUT_VIOLATION,
    TIMEOUT,
    RATE_LIMITED,
    DEPENDENCY_FAILURE,
    INTERNAL_SDK_ERROR
}
