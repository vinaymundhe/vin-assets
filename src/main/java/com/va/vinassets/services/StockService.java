package com.va.vinassets.services;

import com.va.vinassets.dao.StockRepository;
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
    private final String apiHostPrice = "yahoo-finance166.p.rapidapi.com";
    private final String priceUrl = "https://yahoo-finance166.p.rapidapi.com/api/stock/get-price";

    // Method 2: Fetch current stock price
    @Async("asyncExecutor")
    public CompletableFuture<String> getCurrentStockSummary(String symbol) throws IOException {
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String url = priceUrl + "?region=US&symbol=" + symbol;

        CompletableFuture<String> result = client.prepare("GET", url)
                .setHeader("x-rapidapi-key", apiKey)
                .setHeader("x-rapidapi-host", apiHostPrice)
                .execute()
                .toCompletableFuture()
                .thenApply(response -> {
                    String responseBody = response.getResponseBody();
                    return parseStockSummaryFromResponse(responseBody);
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

    // Utility method to parse key fields from the API response
    private String parseStockSummaryFromResponse(String responseBody) {
        try {
            JsonNode root = new ObjectMapper().readTree(responseBody);
            JsonNode priceNode = root.path("quoteSummary").path("result").get(0).path("price");

            String symbol = priceNode.path("symbol").asText();
            String companyName = priceNode.path("shortName").asText();
            String exchangeName = priceNode.path("exchangeName").asText();
            double currentPrice = priceNode.path("regularMarketPrice").path("raw").asDouble();
            String currency = priceNode.path("currency").asText();
            double priceChange = priceNode.path("regularMarketChange").path("raw").asDouble();
            double priceChangePercent = priceNode.path("regularMarketChangePercent").path("raw").asDouble();
            double dayHigh = priceNode.path("regularMarketDayHigh").path("raw").asDouble();
            double dayLow = priceNode.path("regularMarketDayLow").path("raw").asDouble();
            long marketVolume = priceNode.path("regularMarketVolume").path("raw").asLong();
            double previousClose = priceNode.path("regularMarketPreviousClose").path("raw").asDouble();
            double openPrice = priceNode.path("regularMarketOpen").path("raw").asDouble();
            long marketCap = priceNode.path("marketCap").path("raw").asLong();

            // Format the summary
            return String.format("Symbol: %s\nCompany: %s\nExchange Name: %s\nCurrent Price: %.2f\nCurrency: %s\nChange: %.2f (%.2f%%)\nDay High: %.2f\nDay Low: %.2f\nVolume: %d\nPrevious Close: %.2f\nOpen: %.2f\nMarket Cap: %d",
                    symbol, companyName, exchangeName, currentPrice, currency, priceChange, priceChangePercent, dayHigh, dayLow, marketVolume, previousClose, openPrice, marketCap);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing stock data";
        }
    }

    // Utility method to parse the regular market price from the stock summary JSON
    public double parseRegularMarketPrice(String stockSummary) {
        try {
            JsonNode root = new ObjectMapper().readTree(stockSummary);
            return root.path("quoteSummary").path("result").get(0).path("price").path("regularMarketPrice").path("raw").asDouble();
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
