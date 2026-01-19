package com.shawn.taskassistant.port.errors;

import com.shawn.taskassistant.shared.failure.FailureCertainty;


public interface PortFailure {


    Enum<?> reason();


    FailureCertainty certainty();


    String traceId();
}

