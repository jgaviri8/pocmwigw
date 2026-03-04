
package com.sura.pocmwigw.business;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;

import reactor.core.publisher.Mono;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles routing and validation logic for requests to external services.
 * This is the core business logic for the microservice.
 */
@Service
@RefreshScope
@ConfigurationProperties(prefix = "routing")
public class RequestRouter {

    @Autowired
    private Environment environment;

    @Autowired
    private ProductFactory productFactory;

    private Map<String, Object> config = new HashMap<>();

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    @PostConstruct
    public void init() {
        System.out.println("=== RequestRouter PostConstruct ===");
        System.out.println("Config map size: " + config.size());
        System.out.println("Config map contents: " + config);
        System.out.println("routing.config.response from Environment: " + environment.getProperty("routing.config.response"));
        System.out.println("===================================");
    }

    /**
     * Validates the incoming request data reactively.
     * @param requestData The request data to validate
     * @return Mono emitting true if valid, false otherwise
     */
    public Mono<Boolean> validateRequest(Map<String, Object> requestData) {
        // Reactive validation logic
        return Mono.just(requestData != null && !requestData.isEmpty());
    }

    /**
     * Determines which service to route the request to at runtime, reactively.
     * @param requestData The request data
     * @return Mono emitting the name or identifier of the target service
     */
    public Mono<String> routeRequest(Map<String, Object> requestData) {
        System.out.println(environment.getProperty("gateway.routingLog", "Routing log not found"));
        // Reactive routing logic based on requestData
        if (requestData != null && requestData.containsKey("serviceType")) {
            return Mono.just(requestData.get("serviceType").toString());
        }
        Mono<String> routingResponse = Mono.just("result from defaultService");
        System.out.println("Routing config contents:");
        config.keySet().forEach(key -> {
            System.out.println("Config key: " + key + " = " + config.get(key));
        });
        if(config != null && config.containsKey("products")){
            Collection<Map<String, Object>> productCatalog = ((Map<?, Map<String, Object>>) config.get("products")).values();
            List<Product<?>> configuredProducts = new ArrayList<>();
            for(Map<String, Object> productConfig : productCatalog){
                Map<String, Object> quoterConfig = (Map<String, Object>) productConfig.get("quoter");
                Product<?> productInstance = productFactory.createProduct(quoterConfig, productConfig.get("name").toString());
                configuredProducts.add(productInstance);
            }
            Map<String, Object> requestedProductMap = (Map<String, Object>) requestData.get("product");
            String requestedProductName = requestedProductMap.get("name").toString();
            requestedProductMap.keySet().forEach(key -> {
                System.out.println("Requested product parameter: " + key + " = " + requestedProductMap.get(key));
            });
            Product<?> selectedProduct = configuredProducts.stream().filter(p -> p.getName().equalsIgnoreCase(requestedProductName)).findFirst().orElse(null);
            if(selectedProduct != null){
                routingResponse = selectedProduct.getQuoter().getQuote(requestedProductMap).map(quote -> quote.toString());
            } else {
                routingResponse = Mono.just("Requested product not found, routed to defaultService");
            }
        }
        return routingResponse;
    }
}
