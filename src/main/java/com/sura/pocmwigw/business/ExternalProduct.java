package com.sura.pocmwigw.business;

public class ExternalProduct extends Product<String> {
    public ExternalProduct(String name, QuoterBehavior<String> quoter) {
        super(name, quoter);
    }
    
}
