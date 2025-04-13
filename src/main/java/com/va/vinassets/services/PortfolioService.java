package com.va.vinassets.services;

import com.va.vinassets.dao.PortfolioRepository;
import com.va.vinassets.dao.StockRepository;
import com.va.vinassets.models.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockRepository stockRepository;

    public String addStockToPortfolio(String symbol, double quantity, double purchasePrice, LocalDate purchaseDate) {

        Portfolio existingPortfolio = portfolioRepository.findBySymbol(symbol);

        if (existingPortfolio != null) {
            double previousQuantity = existingPortfolio.getQuantity();
            double previousPurchasePrice = existingPortfolio.getPurchasePrice();

            double newTotalQuantity = previousQuantity + quantity;
            double weightedAvgPrice = ((previousPurchasePrice * previousQuantity) + (purchasePrice * quantity)) / newTotalQuantity;

            existingPortfolio.setQuantity(newTotalQuantity);
            existingPortfolio.setPurchasePrice(weightedAvgPrice);
            existingPortfolio.setPurchaseDate(purchaseDate);

            portfolioRepository.save(existingPortfolio);
            return "Updated existing stock in portfolio.";
        } else {
            Portfolio newPortfolio = new Portfolio();
            newPortfolio.setSymbol(symbol);
            newPortfolio.setQuantity(quantity);
            newPortfolio.setPurchasePrice(purchasePrice);
            newPortfolio.setPurchaseDate(purchaseDate);

            portfolioRepository.save(newPortfolio);
            return "Added new stock to portfolio.";
        }

/*
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

 */
    }
}