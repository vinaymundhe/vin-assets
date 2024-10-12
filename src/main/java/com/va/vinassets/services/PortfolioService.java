package com.va.vinassets.services;

import com.va.vinassets.dao.PortfolioRepository;
import com.va.vinassets.models.Portfolio;
import com.va.vinassets.models.PortfolioStock;
import com.va.vinassets.models.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockService stockService; // Reference StockService

    // Method to add a stock to the portfolio
    public CompletableFuture<String> addStockToPortfolio(String userId, String symbol, int quantity, double purchasePrice, LocalDate purchaseDate) throws IOException {
        return stockService.getStockProfile(symbol).thenApply(profileData -> {
            // Find the user's portfolio by userId
            List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
            Portfolio portfolio;

            // Check if there's exactly one portfolio or create a new one
            if (portfolios.size() == 1) {
                portfolio = portfolios.get(0); // Use the existing portfolio
            } else if (portfolios.isEmpty()) {
                portfolio = new Portfolio(userId); // Create new portfolio if not found
            } else {
                throw new RuntimeException("Multiple portfolios found for userId: " + userId);
            }

            // Check if the stock with the same symbol already exists in the portfolio
            Optional<PortfolioStock> existingStock = portfolio.getPortfolioStocks().stream()
                    .filter(portfolioStock -> portfolioStock.getStock().getSymbol().equals(symbol)) // Check symbol in PortfolioStock
                    .findFirst();

            // If stock already exists, return an error message
            if (existingStock.isPresent()) {
                return "Stock with this symbol already exists in the portfolio.";
            }

            // Create a new PortfolioStock and add it to the portfolio
            Stock stock = new Stock(symbol, profileData); // Assuming Stock model has a constructor for symbol and profile
            PortfolioStock portfolioStock = new PortfolioStock(stock, quantity, purchasePrice, purchaseDate);
            portfolio.addPortfolioStock(portfolioStock);  // Add to the portfolio using the method in the Portfolio entity
            portfolioRepository.save(portfolio);  // Save the updated portfolio

            return "Stock added to portfolio successfully.";
        });
    }

    // Method to remove a stock from the portfolio
    public String removeStockFromPortfolio(String userId, String symbol) {
        // Find the user's portfolio
        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
        if (portfolios.size() == 1) {
            Portfolio portfolio = portfolios.get(0); // Use the existing portfolio

            // Find the stock in the portfolio
            Optional<PortfolioStock> stockToRemove = portfolio.getPortfolioStocks().stream()
                    .filter(portfolioStock -> portfolioStock.getStock().getSymbol().equals(symbol))  // Match symbol
                    .findFirst();

            // If stock is found, remove it from the portfolio
            if (stockToRemove.isPresent()) {
                portfolio.removePortfolioStock(stockToRemove.get());
                portfolioRepository.save(portfolio);  // Save the updated portfolio
                return "Stock removed from portfolio.";
            } else {
                return "Stock not found in portfolio.";
            }
        }
        return "Portfolio not found for the user.";
    }

    // Method to calculate PnL (Profit & Loss) for a stock in the portfolio
    public CompletableFuture<Double> calculatePnL(PortfolioStock portfolioStock) throws IOException {
        return stockService.getCurrentStockPrice(portfolioStock.getStock().getSymbol())
                .thenApply(currentPrice -> (currentPrice - portfolioStock.getPurchasePrice()) * portfolioStock.getQuantity());
    }

    // Method to find stock in portfolio by userId and stock symbol
    public Optional<PortfolioStock> findStockInPortfolio(String userId, String symbol) {
        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
        if (portfolios.size() == 1) {
            Portfolio portfolio = portfolios.get(0);
            return portfolio.getPortfolioStocks().stream()
                    .filter(portfolioStock -> portfolioStock.getStock().getSymbol().equals(symbol)) // Match symbol
                    .findFirst();
        }
        return Optional.empty(); // Return empty if portfolio does not exist or multiple portfolios found
    }

    // Method to get the user's portfolio, including overall PnL and invested value
    public CompletableFuture<Portfolio> getUserPortfolio(String userId) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
            return portfolios.isEmpty() ? new Portfolio(userId) : portfolios.get(0); // Return an empty portfolio if not found
        }).thenApplyAsync(portfolio -> {
            double totalPnL = 0;
            double investedValue = 0;

            for (PortfolioStock portfolioStock : portfolio.getPortfolioStocks()) {
                try {
                    double pnl = calculatePnL(portfolioStock).join();  // Calculate PnL for each PortfolioStock
                    totalPnL += pnl; // Accumulate total PnL
                    investedValue += portfolioStock.getQuantity() * portfolioStock.getPurchasePrice(); // Total invested value
                } catch (IOException e) {
                    e.printStackTrace(); // Handle exception
                }
            }

            // Set total PnL and invested value in the Portfolio model
            portfolio.setTotalPnL(totalPnL); // Set total PnL
            portfolio.setInvestedValue(investedValue); // Set total invested value
            return portfolio;
        });
    }
}
