package com.va.vinassets.services;

import com.va.vinassets.dao.PortfolioRepository;
import com.va.vinassets.models.Crypto;
import com.va.vinassets.models.Portfolio;
import com.va.vinassets.models.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockService stockService;

    @Autowired
    private CryptoService cryptoService;

    // Add stock to the portfolio
    public CompletableFuture<String> addStockToPortfolio(String userId, String stockSymbol) throws IOException {
        return stockService.getStockProfile(stockSymbol).thenApply(profileData -> {
            if (profileData.startsWith("Error:")) {
                return profileData; // Handle error case
            }

            Stock stock = new Stock(stockSymbol, profileData);
            Optional<Portfolio> portfolioOpt = portfolioRepository.findByUserId(userId);
            Portfolio portfolio;

            // Initialize portfolio if it doesn't exist
            if (portfolioOpt.isPresent()) {
                portfolio = portfolioOpt.get();
            } else {
                portfolio = new Portfolio(userId, new ArrayList<>(), new ArrayList<>()); // Initialize stocks and crypto lists
            }

            portfolio.addStock(stock);
            portfolioRepository.save(portfolio);

            return "Stock added to portfolio successfully.";
        }).exceptionally(ex -> {
            // Handle exceptions here (e.g., log them)
            return "Error adding stock to portfolio: " + ex.getMessage();
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
}


