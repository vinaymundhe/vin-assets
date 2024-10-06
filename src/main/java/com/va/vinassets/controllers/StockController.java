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

    // New endpoint to get a user's portfolio (stocks + cryptos)
    @GetMapping("/portfolio/{userId}")
    public CompletableFuture<Portfolio> getPortfolio(@PathVariable String userId) {
        return portfolioService.getUserPortfolio(userId);
    }

    @GetMapping("/price/{symbol}")
    public CompletableFuture<Double> getCurrentStockPrice(@PathVariable String symbol) throws IOException {
        return stockService.getCurrentStockPrice(symbol);
    }

    // New endpoint to get P&L of a stock
    @GetMapping("/pnl/{symbol}")
    public CompletableFuture<Double> getPnL(@PathVariable String symbol) throws IOException {
        return stockService.calculatePnL(symbol);
    }
}
