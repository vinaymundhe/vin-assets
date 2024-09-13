package com.va.vinassets.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @GetMapping("/{symbol}")
    public String getStockPrice(@PathVariable String symbol) {
        // Mock data: In reality, fetch stock data from an API
        return "Stock Price of " + symbol + " is 150 USD";
    }
}

