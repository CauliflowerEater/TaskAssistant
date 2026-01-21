package com.shawn.taskassistant.support.failure;

import com.shawn.taskassistant.shared.failure.FailureCertainty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemFailure {
    private FailureSemantic failureSemantic;
    private FailureCertainty failureCertainty;
}

