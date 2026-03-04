package com.sura.pocmwigw.business;

import java.util.Map;
import reactor.core.publisher.Mono;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class QuoteWithCalculator implements QuoterBehavior<Double> {

    private final String expression;

    public QuoteWithCalculator(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression must not be null or empty");
        }
        this.expression = expression;
    }

    @Override
    public Mono<Double> getQuote(Map<String, Object> parameteresMap) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (Map.Entry<String, Object> entry : parameteresMap.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        @SuppressWarnings("null")
        Double result = parser.parseExpression(expression).getValue(context, Double.class);
        return Mono.justOrEmpty(result);
    }
    
}
