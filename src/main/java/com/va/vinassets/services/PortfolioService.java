package com.va.vinassets.services;

import com.va.vinassets.dao.PortfolioRepository;
import com.va.vinassets.models.Breakdown;
import com.va.vinassets.models.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
        double invested = 0;
        if (existingPortfolio != null) {
            double previousQuantity = existingPortfolio.getQuantity();
            double previousAvgPrice = existingPortfolio.getAveragePrice();

            invested = existingPortfolio.getInvested() + (quantity * purchasePrice);
            double newTotalQuantity = previousQuantity + quantity;
            double weightedAvgPrice = ((previousAvgPrice * previousQuantity) + (purchasePrice * quantity)) / newTotalQuantity;

            existingPortfolio.setQuantity(newTotalQuantity);
            existingPortfolio.setAveragePrice(weightedAvgPrice);
            existingPortfolio.setInvested(invested);

            addTransactionToBreakdown(existingPortfolio, symbol, quantity, purchasePrice, purchaseDate);
            portfolioRepository.save(existingPortfolio);

            return "Updated existing stock in portfolio.";
        } else {
            Portfolio newPortfolio = new Portfolio();

            invested = quantity * purchasePrice;

            newPortfolio.setSymbol(symbol);
            newPortfolio.setQuantity(quantity);
            newPortfolio.setAveragePrice(purchasePrice);
            newPortfolio.setInvested(invested);

            addTransactionToBreakdown(newPortfolio, symbol, quantity, purchasePrice, purchaseDate);
            portfolioRepository.save(newPortfolio);

            return "Added new stock to portfolio.";
        }
    }

    public String deleteStockFromPortfolio(String symbol) {
        Portfolio fetchStockToDelete = portfolioRepository.findBySymbol(symbol);
        String result;
        if (fetchStockToDelete != null) {
            portfolioRepository.delete(fetchStockToDelete);
            result = "Deleted " + fetchStockToDelete.getSymbol();
        } else {
            result = symbol + " stock doesn't exist in portfolio.";
        }
        return result;
    }

    public List<Portfolio> getCompletePortfolio() {
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        getCompletePnL(portfolioList, null);

        return portfolioList;
    }

    public Portfolio getStockBreakdown(String symbol){
        Portfolio stockBreakdown = portfolioRepository.findBySymbol(symbol);
        getCompletePnL(null, stockBreakdown);

        return stockBreakdown;
    }

    private void getCompletePnL(List<Portfolio> portfolioList, Portfolio stockBreakdown) {

        if (portfolioList != null && !portfolioList.isEmpty()) {
            for (Portfolio stock : portfolioList) {
                double qty = stock.getQuantity();
                double buyPrice = stock.getAveragePrice();
                double purchaseValue = qty * buyPrice;
                String symbol = stock.getSymbol();

                CompletableFuture<Double> priceFuture = stockService.getCurrentStockPrice(symbol);
                double currentMarketPrice = priceFuture.join();
                double currentValue = currentMarketPrice * qty;
                double pnL = currentValue - purchaseValue;

                stock.setCurrentValue(currentValue);
                stock.setCurrentPrice(currentMarketPrice);
                stock.setProfitAndLoss(pnL);
            }
        }

        if (stockBreakdown != null) {
            double qty = stockBreakdown.getQuantity();
            double buyPrice = stockBreakdown.getAveragePrice();
            double purchaseValue = qty * buyPrice;
            String symbol = stockBreakdown.getSymbol();

            CompletableFuture<Double> priceFuture = stockService.getCurrentStockPrice(symbol);
            double currentMarketPrice = priceFuture.join();
            double currentValue = currentMarketPrice * qty;
            double pnL = currentValue - purchaseValue;

            stockBreakdown.setCurrentValue(currentValue);
            stockBreakdown.setCurrentPrice(currentMarketPrice);
            stockBreakdown.setProfitAndLoss(pnL);
        }
    }

    private void addTransactionToBreakdown(Portfolio portfolio, String symbol, double quantity, double purchasePrice, LocalDate purchaseDate) {
        Breakdown breakdown = new Breakdown();
        breakdown.setQuantity(quantity);
        breakdown.setPrice(purchasePrice);
        breakdown.setTransactionDate(purchaseDate);

        if (purchaseDate == null) {
            throw new IllegalArgumentException("Purchase date cannot be null");
        }

        LocalDate today = LocalDate.now();
        breakdown.setDaysFromTransaction(ChronoUnit.DAYS.between(purchaseDate, today));

        CompletableFuture<Double> priceFuture = stockService.getCurrentStockPrice(symbol);
        double currentMarketPrice = priceFuture.join();
        double currentValue = currentMarketPrice * quantity;
        double purchaseValue = quantity * purchasePrice;
        double pnL = currentValue - purchaseValue;
        breakdown.setPnLSinceBuyPrice(pnL);

        if (portfolio.getBreakdownList() == null){
            List<Breakdown> breakdownList = new ArrayList<>();
            portfolio.setBreakdownList(breakdownList);
        }
        portfolio.getBreakdownList().add(breakdown);
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