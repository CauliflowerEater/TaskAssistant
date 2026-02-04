package com.shawn.taskassistant.infra.webflux;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.*;

import com.shawn.taskassistant.support.context.ContextKeys;

import reactor.core.publisher.Mono;

@Component
public class TracingWebFilter implements WebFilter {

  private final Tracer tracer;

  public TracingWebFilter(Tracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest req = exchange.getRequest();
    String spanName = req.getMethod() + " " + req.getPath().value();

    Span root = tracer.spanBuilder(spanName)
        .setSpanKind(SpanKind.SERVER)
        .startSpan();

    String httpMethod = req.getMethod() != null ? req.getMethod().name() : "UNKNOWN";
    root.setAttribute("http.method", httpMethod);
    root.setAttribute("http.route", req.getPath().value());

    String traceId = root.getSpanContext().getTraceId();
    exchange.getAttributes().put(ContextKeys.EXCHANGE_TRACE_ID, traceId);

    return chain.filter(exchange)
        .contextWrite(ctx -> ctx.put(ContextKeys.CTX_SPAN, root))
        .doOnError(err -> {
          root.recordException(err);
          root.setStatus(StatusCode.ERROR);
        })
        .doOnSuccess(v -> root.setStatus(StatusCode.OK))
        .doFinally(sig -> root.end());
  }
}