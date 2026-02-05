package com.shawn.taskassistant.infra.web.webflux;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

@Component
public class ReactorMdcHook {
  private static final String KEY = "mdc-bridge";

  @PostConstruct
  public void init() {
    Hooks.onEachOperator(KEY, Operators.lift((sc, sub) -> MdcBridge.wrap(sub)));
  }

  @PreDestroy
  public void shutdown() {
    Hooks.resetOnEachOperator(KEY);
  }
}