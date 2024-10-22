package com.va.vinassets.controllers;

import com.va.vinassets.models.Portfolio;
import com.va.vinassets.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    // Endpoint to add a stock to the portfolio (manually adding stock details)
    @PostMapping("/add")
    public ResponseEntity<String> addStockToPortfolio(
            @RequestParam String userId,
            @RequestParam String symbol,
            @RequestParam int quantity,
            @RequestParam double purchasePrice,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate) {

        // Call service to add stock
        String result = portfolioService.addStockToPortfolio(userId, symbol, quantity, purchasePrice, purchaseDate);
        return new ResponseEntity<>(result, HttpStatus.OK); // Return the result directly
    }

    // Endpoint to remove a stock from the portfolio
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeStockFromPortfolio(
            @RequestParam String userId,
            @RequestParam String symbol) {

        // Call service to remove the stock
        String result = portfolioService.removeStockFromPortfolio(userId, symbol);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Endpoint to get the user's portfolio and calculate total PnL
    @GetMapping("/user/{userId}")
    public ResponseEntity<Portfolio> getUserPortfolio(
            @PathVariable String userId,
            @RequestParam List<Double> currentMarketPrices) {

        // Fetch the portfolio and calculate PnL using the provided current market prices
        Portfolio portfolio = portfolioService.getUserPortfolio(userId, currentMarketPrices);
        if (portfolio.getPortfolioStocks().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if no stocks in the portfolio
        }
        return new ResponseEntity<>(portfolio, HttpStatus.OK); // Return 200 with the portfolio details
    }

    // Endpoint to find a specific stock in the portfolio
    @GetMapping("/user/{userId}/stock/{symbol}")
    public ResponseEntity<String> findStockInPortfolio(
            @PathVariable String userId,
            @PathVariable String symbol) {

        var stockOpt = portfolioService.findStockInPortfolio(userId, symbol);
        return stockOpt
                .map(portfolioStock -> ResponseEntity.ok("Stock found: " + portfolioStock.getStock().getSymbol()))
                .orElse(ResponseEntity.notFound().build()); // Return 404 if the stock is not found
    }
}
