package com.shawn.taskassistant.port.taskAnalyzeLLM.errors;

import com.shawn.taskassistant.port.errors.PortFailure;
import com.shawn.taskassistant.shared.failure.FailureCertainty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnalyzeLLMPortFailure implements PortFailure {
    private TaskAnalyzeLLMPortFailureReason reason;
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
