package com.va.vinassets.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private StockService stockService; // Inject StockService

    // Method to add a stock to the portfolio
    public CompletableFuture<String> addStockToPortfolio(String userId, String symbol, int quantity, double purchasePrice, LocalDate purchaseDate) throws IOException {
        return stockService.getCurrentStockSummary(symbol).thenApply(response -> {
            // Parse the response to create a Stock object
            Stock stock = parseStockFromResponse(response);

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

            // Ensure stock is persisted before adding it to PortfolioStock
            if (stock != null) {
                stockService.saveStock(stock); // Save the stock to the database

                // Create a new PortfolioStock and add it to the portfolio
                PortfolioStock portfolioStock = new PortfolioStock(stock, quantity, purchasePrice, purchaseDate);
                portfolio.addPortfolioStock(portfolioStock); // Add to the portfolio using the method in the Portfolio entity

                // Save the updated portfolio with the new stock
                portfolioRepository.save(portfolio);

                return "Stock added to portfolio successfully.";
            } else {
                return "Failed to retrieve stock summary.";
            }
        });
    }

    // Utility method to parse Stock from the API response
    private Stock parseStockFromResponse(String responseBody) {
        try {
            JsonNode root = new ObjectMapper().readTree(responseBody);
            JsonNode priceNode = root.path("quoteSummary").path("result").get(0).path("price");

            String symbol = priceNode.path("symbol").asText();
            String companyName = priceNode.path("shortName").asText();
            double currentPrice = priceNode.path("regularMarketPrice").path("raw").asDouble();
            String currency = priceNode.path("currency").asText();

            // Create and return a new Stock object directly
            return new Stock(null, symbol, companyName, currentPrice, currency);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle error appropriately
        }
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
        return stockService.getCurrentStockSummary(portfolioStock.getStock().getSymbol())
                .thenApply(stockSummary -> {
                    double regularMarketPrice = stockService.parseRegularMarketPrice(stockSummary);
                    return (regularMarketPrice - portfolioStock.getPurchasePrice()) * portfolioStock.getQuantity();
                });
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
    public CompletableFuture<Portfolio> getUserPortfolio(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
            return portfolios.isEmpty() ? new Portfolio(userId) : portfolios.get(0); // Return an empty portfolio if not found
        }).thenComposeAsync(portfolio -> {
            // List of futures to calculate PnL for each stock
            List<CompletableFuture<Void>> pnlFutures = portfolio.getPortfolioStocks().stream()
                    .map(portfolioStock -> {
                        try {
                            return calculatePnL(portfolioStock)
                                    .thenAccept(pnl -> {
                                        // Update the total PnL and invested value
                                        portfolio.setTotalPnL(portfolio.getTotalPnL() + pnl);
                                        portfolio.setInvestedValue(
                                                portfolio.getInvestedValue() + (portfolioStock.getQuantity() * portfolioStock.getPurchasePrice())
                                        );
                                    });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList(); // Collect to a list of CompletableFuture<Void>

            // Combine all futures and return the portfolio when all PnL calculations are done
            return CompletableFuture.allOf(pnlFutures.toArray(new CompletableFuture[0]))
                    .thenApply(ignored -> portfolio);
        });
    }
}