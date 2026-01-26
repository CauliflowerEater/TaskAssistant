package com.shawn.taskassistant.infra.httpclient;

import com.shawn.taskassistant.support.context.ContextKeys;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient aiWebClient(
      WebClient.Builder builder,
      Tracer tracer,
      @Value("${ai.base-url}") String baseUrl
  ) {

    ExchangeFilterFunction tracingFilter = (request, next) ->
        Mono.deferContextual(ctxView -> {
          Span parent = ctxView.getOrDefault(ContextKeys.CTX_SPAN, Span.current());

          String spanName = request.method() + " " + request.url().getHost();
          Span clientSpan = tracer.spanBuilder(spanName)
              .setParent(Context.current().with(parent))
              .setSpanKind(SpanKind.CLIENT)
              .startSpan();

          clientSpan.setAttribute("http.method", request.method().name());
          clientSpan.setAttribute("http.url", request.url().toString());

          return next.exchange(request)
              .doOnSuccess(resp -> clientSpan.setAttribute("http.status_code", resp.statusCode().value()))
              .doOnError(err -> {
                clientSpan.recordException(err);
                clientSpan.setStatus(StatusCode.ERROR);
              })
              .doFinally(sig -> clientSpan.end())
              .contextWrite(ctx -> ctx.put(ContextKeys.CTX_SPAN, clientSpan))
              .contextWrite(ctx -> ctx.put("key","value"));
        });

    return builder
        .baseUrl(baseUrl)
        .filter(tracingFilter)
        .build();
  }
}