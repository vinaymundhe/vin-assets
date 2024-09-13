package com.va.vinassets.models;

public class Investment {
    private String symbol;
    private double quantity;
    private double currentPrice;

    public Investment(String symbol, double quantity, double currentPrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
    }
    // Getters and setters omitted for brevity
}

