package com.shawn.taskassistant.port.errors;

import com.shawn.taskassistant.shared.failure.FailureCertainty;

public interface PortFailure<R> {
    R getReason();

    FailureCertainty getCertainty();

    String getTraceId();
}
