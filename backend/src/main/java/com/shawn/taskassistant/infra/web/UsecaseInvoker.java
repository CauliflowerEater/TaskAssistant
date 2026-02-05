package com.shawn.taskassistant.infra.web;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;

import org.slf4j.MDC;

import com.shawn.taskassistant.support.context.ContextKeys;

public final class UsecaseInvoker {

  private UsecaseInvoker() {}

  public static <T> Mono<T> invoke(Callable<T> task) {
    return Mono.deferContextual(ctxView -> {
      Span span = ctxView.getOrDefault(ContextKeys.CTX_SPAN, Span.current());

      return Mono.fromCallable(() -> {
            var spanContext = span.getSpanContext();
            MDC.put("traceId", spanContext.getTraceId());
            MDC.put("spanId", spanContext.getSpanId());
            try (Scope scope = Context.current().with(span).makeCurrent()) {
              return task.call();
            } finally {
              MDC.remove("traceId");
              MDC.remove("spanId");
            }
          })
          .subscribeOn(Schedulers.boundedElastic());
    });
  }
}