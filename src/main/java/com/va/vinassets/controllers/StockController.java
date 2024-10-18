package com.va.vinassets.controllers;

import com.va.vinassets.models.Portfolio;
import com.va.vinassets.services.PortfolioService;
import com.va.vinassets.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/profile/{symbol}")
    public CompletableFuture<String> getStockProfile(@PathVariable String symbol) throws IOException {
        return stockService.getStockProfile(symbol);
    }

    // Method to get stock summary (price, volume, etc.)
    @GetMapping("/summary/{symbol}")
    public CompletableFuture<String> getStockSummary(@PathVariable String symbol) throws IOException {
        return stockService.getCurrentStockSummary(symbol);
    }

//    // New endpoint to get a user's portfolio (stocks + cryptos)
//    @GetMapping("/portfolio/{userId}")
//    public CompletableFuture<Portfolio> getPortfolio(@PathVariable String userId) {
//        return portfolioService.getUserPortfolio(userId);
//    }
}
