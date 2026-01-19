package com.shawn.taskassistant.port.structured_generation.errors;

import com.shawn.taskassistant.port.errors.PortFailure;
import com.shawn.taskassistant.shared.failure.FailureCertainty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructuredGenerationPortFailure implements PortFailure {
    private StructuredGenerationPortFailureReason reason;
    private FailureCertainty certainty;
    private int attempts;
    private int maxAttempts;
    private String traceId;

    @Override
    public Enum<?> reason() {
        return reason;
    }

    @Override
    public FailureCertainty certainty() {
        return certainty;
    }

    @Override
    public String traceId() {
        return traceId;
    }
}


