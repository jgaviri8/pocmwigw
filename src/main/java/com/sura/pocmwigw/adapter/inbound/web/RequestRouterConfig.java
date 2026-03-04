package com.sura.pocmwigw.adapter.inbound.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RequestRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routeRequest(RequestRouterHandler handler) {
        return RouterFunctions.route()
            .POST("/route/{*path}", handler::route)
            .build();
    }
}
