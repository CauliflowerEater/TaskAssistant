package com.shawn.taskassistant.support.failure;

import java.util.Map;
import com.shawn.taskassistant.shared.failure.FailureCertainty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailureEnvelope {
    private String usecaseSemantic;
    private FailureSemantic failureSemantic;
    private FailureCertainty certainty;
    private String traceId;
    private String occurredAt;
    private Map<String, Object> context;
}

