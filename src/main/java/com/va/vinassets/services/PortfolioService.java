package com.va.vinassets.services;

import com.va.vinassets.dao.PortfolioRepository;
import com.va.vinassets.models.Portfolio;
import com.va.vinassets.models.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockService stockService; // Reference StockService

    @Autowired
    private CryptoService cryptoService;

    // New method to add a stock to the portfolio
    public CompletableFuture<String> addStockToPortfolio(String userId, String symbol, int quantity, double purchasePrice, LocalDate purchaseDate) throws IOException {
        return stockService.getStockProfile(symbol).thenApply(profileData -> {
            // Find the user's portfolio by userId
            Optional<Portfolio> portfolioOpt = portfolioRepository.findByUserId(userId);
            Portfolio portfolio;

            if (portfolioOpt.isPresent()) {
                portfolio = portfolioOpt.get();
            } else {
                portfolio = new Portfolio(userId, new ArrayList<>(), new ArrayList<>());
            }

            // Check if the stock with the same symbol already exists in the portfolio
            Optional<Stock> existingStock = portfolio.getStocks().stream()
                    .filter(stock -> stock.getSymbol().equals(symbol))  // Use the 'symbol' variable passed to the method
                    .findFirst();

            // If stock already exists, return an error message
            if (existingStock.isPresent()) {
                return "Stock with this symbol already exists in the portfolio.";
            }

            // Create and add new stock to portfolio
            Stock stock = new Stock(symbol, profileData, quantity, purchasePrice, purchaseDate);
            portfolio.addStock(stock);
            portfolioRepository.save(portfolio);  // Save the updated portfolio

            return "Stock added to portfolio successfully.";
        });
    }



    // Retrieve the user's combined portfolio
    public CompletableFuture<Portfolio> getUserPortfolio(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            return portfolioRepository.findByUserId(userId)
                    .orElse(new Portfolio(userId, new ArrayList<>(), new ArrayList<>())); // Return an empty portfolio
        }).exceptionally(ex -> {
            // Handle exceptions here (e.g., log them)
            return new Portfolio(userId, new ArrayList<>(), new ArrayList<>()); // Return an empty portfolio on error
        });
    }

    public Optional<Stock> findStockInPortfolio(String userId, String symbol) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findByUserId(userId);
        if (portfolioOpt.isPresent()) {
            Portfolio portfolio = portfolioOpt.get();
            return portfolio.getStocks().stream()
                    .filter(stock -> stock.getSymbol().equals(symbol))
                    .findFirst();
        }
        return Optional.empty(); // Return empty if portfolio does not exist
    }

}
