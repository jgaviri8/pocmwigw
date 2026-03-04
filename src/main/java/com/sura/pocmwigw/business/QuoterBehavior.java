package com.sura.pocmwigw.business;

import java.util.Map;
import reactor.core.publisher.Mono;

public interface QuoterBehavior<E> {
    Mono<E> getQuote(Map<String, Object> parameteresMap);
}
