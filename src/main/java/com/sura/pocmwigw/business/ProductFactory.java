package com.sura.pocmwigw.business;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductFactory {

    private WebClient.Builder builder;

    public ProductFactory(WebClient.Builder builder) {
        this.builder = builder;
    }

    public Product<?> createProduct(Map<String, Object> quoterConfig, String name) {
        Product<?> product = null;
        String quoterType = quoterConfig.get("type").toString();
        if("sync http".equalsIgnoreCase(quoterType)) {
            Map<String, Object> configMap = (Map<String, Object>) quoterConfig.get("config");
            QuoterBehavior<String> quoter = new QuoteWithSyncService(
                builder,
                configMap.get("urlBase").toString(),
                configMap.get("uri").toString(),
                configMap.get("method").toString(),
                ((Integer) configMap.get("timeout")).longValue()
            );
            product = new ExternalProduct(name, quoter);
        } else if("calculator".equalsIgnoreCase(quoterType)) {
            String expression = ((Map<String, Object>) quoterConfig.get("config")).get("expression").toString();
            QuoterBehavior<Double> quoter = new QuoteWithCalculator(expression);
            product = new InternalProduct(name, quoter);
        }
        return product;
    }
}
