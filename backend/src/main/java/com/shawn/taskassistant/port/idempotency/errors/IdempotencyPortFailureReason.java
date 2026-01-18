package com.shawn.taskassistant.port.idempotency.errors;

public enum IdempotencyPortFailureReason {
    INVALID_INPUT,
    STORAGE_UNAVAILABLE,
    STORAGE_TIMEOUT,
    DATA_CORRUPTION,
    INTERNAL_ERROR
}

