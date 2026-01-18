package com.shawn.taskassistant.port.structured_generation.errors;

import com.shawn.taskassistant.shared.failure.FailureCertainty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructuredGenerationPortFailure {
    private StructuredGenerationPortFailureReason reason;
    private FailureCertainty certainty;
    private int attempts;
    private int maxAttempts;
    private String traceId;
}


