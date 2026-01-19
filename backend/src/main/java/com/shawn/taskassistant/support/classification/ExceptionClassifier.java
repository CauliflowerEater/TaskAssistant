package com.shawn.taskassistant.support.classification;

import com.shawn.taskassistant.port.errors.PortFailure;
import com.shawn.taskassistant.support.classification.registry.PortFailureSemanticRegistry;
import com.shawn.taskassistant.support.failure.FailureSemantic;
import com.shawn.taskassistant.support.failure.SystemFailure;

import java.util.Objects;

/**
 * Classifies port-level technical failures into system-level semantics.
 *
 * Rules:
 * - Input MUST be PortFailure (already translated by adapter/port).
 * - MUST NOT unwrap Throwable chains.
 * - MUST NOT inspect raw SDK/HTTP/SQL exceptions.
 * - Pure mapping: (portFailureType, reason) -> FailureSemantic
 */
public final class ExceptionClassifier {

    private final PortFailureSemanticRegistry registry;

    public ExceptionClassifier(PortFailureSemanticRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
    }

    public SystemFailure classify(PortFailure portFailure) {
        Objects.requireNonNull(portFailure, "portFailure");

        FailureSemantic semantic = registry.lookup(portFailure.getClass(), portFailure.reason());
        if (semantic == null) {
            semantic = FailureSemantic.UNKNOWN;
        }

        return new SystemFailure(semantic, portFailure.certainty());
    }
}