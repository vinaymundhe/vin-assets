package com.va.vinassets.exceptions;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String symbol) {
            super("Stock symbol not found: " + symbol);
    }
}
