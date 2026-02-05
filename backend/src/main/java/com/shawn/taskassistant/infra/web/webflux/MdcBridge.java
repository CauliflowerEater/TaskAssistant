package com.shawn.taskassistant.infra.web.webflux;

import io.opentelemetry.api.trace.Span;
import org.slf4j.MDC;

import com.shawn.taskassistant.support.context.ContextKeys;

import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

public final class MdcBridge {
  private MdcBridge() {}

  public static <T> CoreSubscriber<T> wrap(CoreSubscriber<T> actual) {
    return new CoreSubscriber<>() {
      @Override public Context currentContext() { return actual.currentContext(); }

      @Override public void onSubscribe(org.reactivestreams.Subscription s) { actual.onSubscribe(s); }

      @Override public void onNext(T t) { withMdc(actual.currentContext(), () -> actual.onNext(t)); }

      @Override public void onError(Throwable t) { withMdc(actual.currentContext(), () -> actual.onError(t)); }

      @Override public void onComplete() { withMdc(actual.currentContext(), actual::onComplete); }
    };
  }

  private static void withMdc(Context ctx, Runnable r) {
    Span span = ctx.getOrDefault(ContextKeys.CTX_SPAN, Span.current());
    var sc = span.getSpanContext();

    MDC.put("traceId", sc.getTraceId());
    MDC.put("spanId", sc.getSpanId());
    try {
      r.run();
    } finally {
      MDC.remove("traceId");
      MDC.remove("spanId");
    }
  }
}