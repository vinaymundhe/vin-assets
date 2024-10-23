package com.va.vinassets.services;

import com.va.vinassets.dao.PortfolioRepository;
import com.va.vinassets.dao.StockRepository;
import com.va.vinassets.models.Portfolio;
import com.va.vinassets.models.PortfolioStock;
import com.va.vinassets.models.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockRepository stockRepository;

    // Method to add a stock to the portfolio
    public String addStockToPortfolio(String userId, String symbol, int quantity, double purchasePrice, LocalDate purchaseDate) {
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

        // Create a new Stock object with the symbol
        Stock stock = new Stock(symbol); // No need for extra info right now

        // Create a new PortfolioStock and add it to the portfolio
        PortfolioStock portfolioStock = new PortfolioStock(stock, quantity, purchasePrice, purchaseDate);
        portfolio.addPortfolioStock(portfolioStock); // Add to the portfolio using the method in the Portfolio entity

        // Save the updated portfolio with the new stock
        portfolioRepository.save(portfolio);

        return "Stock added to portfolio successfully.";
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
    public double calculatePnL(PortfolioStock portfolioStock, double currentMarketPrice) {
        return (currentMarketPrice - portfolioStock.getPurchasePrice()) * portfolioStock.getQuantity();
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
    public Portfolio getUserPortfolio(String userId, List<Double> currentMarketPrices) {
        List<Portfolio> portfolios = portfolioRepository.findByUserId(userId);
        Portfolio portfolio = portfolios.isEmpty() ? new Portfolio(userId) : portfolios.get(0); // Return an empty portfolio if not found

        // Iterate through portfolio stocks and calculate PnL using provided current market prices
        double totalPnL = 0.0;
        double investedValue = 0.0;

        for (int i = 0; i < portfolio.getPortfolioStocks().size(); i++) {
            PortfolioStock portfolioStock = portfolio.getPortfolioStocks().get(i);
            double currentMarketPrice = currentMarketPrices.get(i);
            double pnl = calculatePnL(portfolioStock, currentMarketPrice);

            totalPnL += pnl;
            investedValue += portfolioStock.getQuantity() * portfolioStock.getPurchasePrice();
        }

        portfolio.setTotalPnL(totalPnL);
        portfolio.setInvestedValue(investedValue);

        return portfolio;
    }
}