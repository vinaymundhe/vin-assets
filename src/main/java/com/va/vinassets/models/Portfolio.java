package com.va.vinassets.models;

import java.util.List;

public class Portfolio {
    private List<Investment> stockInvestments;
    private List<Investment> cryptoInvestments;

    public Portfolio(List<Investment> stockInvestments, List<Investment> cryptoInvestments) {
        this.stockInvestments = stockInvestments;
        this.cryptoInvestments = cryptoInvestments;
    }
    // Getters and setters omitted for brevity
}

