package com.sura.pocmwigw.adapter.inbound.web;

import com.sura.pocmwigw.business.RequestRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * WebFlux handler for routing requests, following hexagonal architecture principles.
 */
@Component
public class RequestRouterHandler {

    private final RequestRouter requestRouter;

    public RequestRouterHandler(RequestRouter requestRouter) {
        this.requestRouter = requestRouter;
    }

    public Mono<ServerResponse> route(ServerRequest serverRequest) {
        System.out.println("Received request to route at path: " + serverRequest.pathVariable("path"));
        return serverRequest.bodyToMono(Map.class)
                .flatMap(requestData -> requestRouter.validateRequest(requestData)
                        .flatMap(valid -> {
                            if (!(Boolean)valid) {
                                return ServerResponse.badRequest().bodyValue("Invalid request");
                            }
                            return requestRouter.routeRequest(requestData)
                                    .flatMap(service -> ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(Map.of("service", service)));
                        })
                );
    }
}
