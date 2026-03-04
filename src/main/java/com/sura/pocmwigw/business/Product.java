package com.sura.pocmwigw.business;

public abstract class Product<T> {
    private final String name;
    private final QuoterBehavior<T> quoter;

    public Product(String name, QuoterBehavior<T> quoter) {
        this.name = name;
        this.quoter = quoter;
    }

    public String getName() {
        return name;
    }
    
    public QuoterBehavior<T> getQuoter() {
        return quoter;
    }
}
