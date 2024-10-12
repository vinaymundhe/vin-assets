package com.va.vinassets.controllers;

import com.va.vinassets.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    // Endpoint to add a stock to the portfolio
    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<String>> addStockToPortfolio(
            @RequestParam String userId,
            @RequestParam String symbol,
            @RequestParam int quantity,
            @RequestParam double purchasePrice,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate) throws IOException {

        // Handle async operation using CompletableFuture
        return portfolioService.addStockToPortfolio(userId, symbol, quantity, purchasePrice, purchaseDate)
                .thenApply(result -> new ResponseEntity<>(result, HttpStatus.OK))  // Handle success
                .exceptionally(ex -> new ResponseEntity<>("Error adding stock: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));  // Handle error
    }

    @DeleteMapping("/remove")
    public String removeStockFromPortfolio(
            @RequestParam String userId,
            @RequestParam String symbol) {
        // Call the service to remove the stock
        return portfolioService.removeStockFromPortfolio(userId, symbol);
    }

    // Endpoint to get the user's portfolio
    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<?>> getUserPortfolio(@PathVariable String userId) {
        return portfolioService.getUserPortfolio(userId)
                .thenApply(portfolio -> {
                    if (portfolio == null || portfolio.getPortfolioStocks().isEmpty()) {
                        return ResponseEntity.notFound().build(); // Return 404 if no portfolio is found
                    }
                    return ResponseEntity.ok(portfolio); // Return 200 with the portfolio if found
                })
                .exceptionally(ex -> {
                    // Log the exception for debugging purposes
                    ex.printStackTrace();
                    return ResponseEntity.badRequest().body(null); // Return 400 for any other errors
                });
    }


    // Endpoint to find a specific stock in the portfolio
    @GetMapping("/user/{userId}/stock/{symbol}")
    public CompletableFuture<ResponseEntity<String>> findStockInPortfolio(
            @PathVariable String userId,
            @PathVariable String symbol) {

        return CompletableFuture.supplyAsync(() -> {
            var stockOpt = portfolioService.findStockInPortfolio(userId, symbol);
            return stockOpt.map(portfolioStock -> ResponseEntity.ok("Stock found: " + portfolioStock.getStock().getSymbol()))
                    .orElse(ResponseEntity.notFound().build());
        });
    }
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

