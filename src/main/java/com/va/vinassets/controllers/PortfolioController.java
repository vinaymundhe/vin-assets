package com.va.vinassets.controllers;

import com.va.vinassets.models.Crypto;
import com.va.vinassets.models.Stock;
import com.va.vinassets.services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    // Get all stocks
    @GetMapping("/stocks")
    public List<Stock> getAllStocks() {
        return portfolioService.getAllStocks();
    }

    // Get all cryptos
    @GetMapping("/cryptos")
    public List<Crypto> getAllCryptos() {
        return portfolioService.getAllCryptos();
    }

    // Add new stock
    @PostMapping("/stocks")
    public Stock addStock(@RequestBody Stock stock) {
        return portfolioService.addStock(stock);
    }

    // Add new crypto
    @PostMapping("/cryptos")
    public Crypto addCrypto(@RequestBody Crypto crypto) {
        return portfolioService.addCrypto(crypto);
    }

    // Update stock
    @PutMapping("/stocks")
    public Stock updateStock(@RequestBody Stock stock) {
        return portfolioService.updateStock(stock);
    }

    // Update crypto
    @PutMapping("/cryptos")
    public Crypto updateCrypto(@RequestBody Crypto crypto) {
        return portfolioService.updateCrypto(crypto);
    }

    // Delete stock
    @DeleteMapping("/stocks/{id}")
    public void deleteStock(@PathVariable Long id) {
        portfolioService.deleteStock(id);
    }

    // Delete crypto
    @DeleteMapping("/cryptos/{id}")
    public void deleteCrypto(@PathVariable Long id) {
        portfolioService.deleteCrypto(id);
    }
}


