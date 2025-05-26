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

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping("/add")
    public ResponseEntity<String> addStockToPortfolio(
            @RequestParam String symbol,
            @RequestParam double quantity,
            @RequestParam double purchasePrice,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate) {

        String result = portfolioService.addStockToPortfolio(symbol, quantity, purchasePrice, purchaseDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteStockFromPortfolio(
            @RequestParam String symbol) {
        String result = portfolioService.deleteStockFromPortfolio(symbol);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Portfolio>> getCompletePortfolio() {
        List<Portfolio> result = portfolioService.getCompletePortfolio();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/stock")
    public ResponseEntity<Portfolio> getStockBreakdown(
            @RequestParam String symbol){
        Portfolio result = portfolioService.getStockBreakdown(symbol);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
     /*

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

     */
}
