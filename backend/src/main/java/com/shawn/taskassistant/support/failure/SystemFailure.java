package com.shawn.taskassistant.support.failure;

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

