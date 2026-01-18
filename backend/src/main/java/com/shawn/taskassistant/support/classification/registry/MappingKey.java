package com.shawn.taskassistant.support.classification.registry;

import java.util.Objects;

public record MappingKey(Class<?> portFailureType, Object reason) {
    public MappingKey {
        Objects.requireNonNull(portFailureType, "portFailureType");
        Objects.requireNonNull(reason, "reason");
    }
}
