package com.sura.pocmwigw.business;

import java.util.Map;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class QuoteWithSyncService implements QuoterBehavior<String> {

    private final String uri;
    private final String method;
    private final long timeout;
    private final WebClient webClient;

    public QuoteWithSyncService(WebClient.Builder builder, String urlBase, String uri, String method, long timeout) {
        this.uri = uri;
        this.method = method;
        this.timeout = timeout;
        this.webClient = builder.baseUrl(urlBase).build();
    }

    @Override
    public Mono<String> getQuote(Map<String, Object> parameteresMap) {
        return webClient.method(org.springframework.http.HttpMethod.valueOf(method))
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class);
    }
    
}
