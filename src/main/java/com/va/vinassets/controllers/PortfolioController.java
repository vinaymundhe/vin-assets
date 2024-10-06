package com.va.vinassets.controllers;

import com.va.vinassets.models.Crypto;
import com.va.vinassets.models.Portfolio;
import com.va.vinassets.models.Stock;
import com.va.vinassets.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    // New endpoint to add stock to the portfolio
    @PostMapping("/add")
    public CompletableFuture<String> addStockToPortfolio(
            @RequestParam String userId,  // Add userId as a parameter
            @RequestParam String symbol,
            @RequestParam int quantity,
            @RequestParam double purchasePrice,
            @RequestParam String purchaseDate) throws IOException {
        LocalDate date = LocalDate.parse(purchaseDate); // Parse purchaseDate from string
        return portfolioService.addStockToPortfolio(userId, symbol, quantity, purchasePrice, date);  // Pass userId to the service
    }

    // Retrieve the user's combined portfolio
    @GetMapping("/user/{userId}")
    public CompletableFuture<Portfolio> getUserPortfolio(@PathVariable String userId) {
        return portfolioService.getUserPortfolio(userId);
    }

//    // Add new crypto (if needed for your design)
//    @PostMapping("/cryptos")
//    public Crypto addCrypto(@RequestBody Crypto crypto) {
//        return portfolioService.addCrypto(crypto);
//    }
//
//    // Update stock (optional based on your design)
//    @PutMapping("/stocks")
//    public Stock updateStock(@RequestBody Stock stock) {
//        return portfolioService.updateStock(stock);
//    }
//
//    // Update crypto (optional based on your design)
//    @PutMapping("/cryptos")
//    public Crypto updateCrypto(@RequestBody Crypto crypto) {
//        return portfolioService.updateCrypto(crypto);
//    }
//
//    // Delete stock (optional based on your design)
//    @DeleteMapping("/stocks/{id}")
//    public void deleteStock(@PathVariable Long id) {
//        portfolioService.deleteStock(id);
//    }
//
//    // Delete crypto (optional based on your design)
//    @DeleteMapping("/cryptos/{id}")
//    public void deleteCrypto(@PathVariable Long id) {
//        portfolioService.deleteCrypto(id);
//    }
}
