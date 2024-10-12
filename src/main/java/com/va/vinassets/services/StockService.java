package com.va.vinassets.services;

import com.va.vinassets.dao.StockRepository;
import com.va.vinassets.exceptions.StockNotFoundException;
import com.va.vinassets.models.Stock;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;  // Inject StockRepository via constructor
    }

    private final String apiKey = "00789db411mshaf17852c311cc33p1bee58jsnc42f3f06271e";  // Use your API key here
    private final String apiHostProfile = "apidojo-yahoo-finance-v1.p.rapidapi.com";
    private final String apiHostPrice = "yahoo-finance166.p.rapidapi.com";
    private final String profileUrl = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v3/get-profile";
    private final String priceUrl = "https://yahoo-finance166.p.rapidapi.com/api/stock/get-price";

    // Method 1: Fetch stock profile
    @Async("asyncExecutor")
    public CompletableFuture<String> getStockProfile(String symbol) throws IOException {
        Stock existingStock = stockRepository.findBySymbol(symbol);
        if (existingStock != null) {
            return CompletableFuture.completedFuture(existingStock.getProfileData());
        }

        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String url = profileUrl + "?symbol=" + symbol + "&region=US&lang=en-US";

        CompletableFuture<String> result = client.prepare("GET", url)
                .setHeader("x-rapidapi-key", apiKey)
                .setHeader("x-rapidapi-host", apiHostProfile)
                .execute()
                .toCompletableFuture()
                .thenApply(response -> {
                    String responseBody = response.getResponseBody();
                    if (responseBody.contains("\"result\":null")) {
                        throw new StockNotFoundException(symbol);
                    }
                    // Save the fetched stock profile to the database
                    Stock stock = new Stock(symbol, responseBody);
                    stockRepository.save(stock);
                    return responseBody;
                })
                .exceptionally(ex -> "Error: " + ex.getMessage());

        result.whenComplete((res, ex) -> {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    // Method 2: Fetch current stock price
    @Async("asyncExecutor")
    public CompletableFuture<Double> getCurrentStockPrice(String symbol) throws IOException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String url = priceUrl + "?region=US&symbol=" + symbol;

        CompletableFuture<Double> result = client.prepare("GET", url)
                .setHeader("x-rapidapi-key", apiKey)
                .setHeader("x-rapidapi-host", apiHostPrice)
                .execute()
                .toCompletableFuture()
                .thenApply(response -> {
                    String responseBody = response.getResponseBody();
                    double currentPrice = parseCurrentPriceFromResponse(responseBody);
                    return currentPrice;
                })
                .exceptionally(ex -> 0.0);  // Return 0.0 in case of error

        result.whenComplete((res, ex) -> {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    // Utility method to parse the current stock price from the API response
    private double parseCurrentPriceFromResponse(String responseBody) {
        try {
            JsonNode root = new ObjectMapper().readTree(responseBody);
            return root.path("price").asDouble();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;  // Return 0.0 in case of an error
        }
    }

    // Save stock in the repository
    public void saveStock(Stock stock) {
        stockRepository.save(stock);
    }
}
