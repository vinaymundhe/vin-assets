package com.va.vinassets.services;

import com.va.vinassets.dao.PortfolioRepository;
import com.va.vinassets.models.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockService stockService;

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
    }

    public String deleteStockFromPortfolio(String symbol) {
        Portfolio fetchStockToDelete = portfolioRepository.findBySymbol(symbol);
            if (fetchStockToDelete != null) {
                portfolioRepository.delete(fetchStockToDelete);
            }
        return "Deleted "+ fetchStockToDelete.getSymbol();
    }

    public List<Portfolio> getCompletePortfolio() {
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        getCompletePnL(portfolioList);

        return portfolioList;
    }

    private void getCompletePnL(List<Portfolio> portfolioList) {
        for (Portfolio stock : portfolioList) {
            double qty = stock.getQuantity();
            double buyPrice = stock.getPurchasePrice();
            double purchaseValue = qty * buyPrice;
            String symbol = stock.getSymbol();

            CompletableFuture<Double> priceFuture = stockService.getCurrentStockPrice(symbol);
            double currentMarketPrice = priceFuture.join();
            double currentValue = currentMarketPrice * qty;
            double pnL = currentValue - purchaseValue;

            stock.setCurrentPrice(currentMarketPrice);
            stock.setProfitAndLoss(pnL);
        }
    }

    /*

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