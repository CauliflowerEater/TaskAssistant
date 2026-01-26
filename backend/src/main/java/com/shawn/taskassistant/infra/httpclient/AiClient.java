package com.shawn.taskassistant.infra.httpclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AiClient {

  private final WebClient aiWebClient;
  private final Logger log = LoggerFactory.getLogger(AiClient.class);

  public AiClient(WebClient aiWebClient) {
    this.aiWebClient = aiWebClient;
  }

  public Mono<AiResp> infer(String text) {
    AiReq req = new AiReq(text);
    return aiWebClient.post()
        .uri("/ai/infer")
        .bodyValue(req)
        .retrieve()
        .bodyToMono(AiResp.class)
        .flatMap(resp -> Mono.deferContextual(ctxView -> {
          log.info("result={}", ctxView.getOrDefault("key", "not found"));
          return Mono.just(resp);
        }));
  }

  public record AiReq(String text) {}
  public record AiResp(String result) {}
}