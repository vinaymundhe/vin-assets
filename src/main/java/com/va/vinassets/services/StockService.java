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

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    private final String apiKey = "00789db411mshaf17852c311cc33p1bee58jsnc42f3f06271e";  // Use your API key here
    private final String apiHost = "apidojo-yahoo-finance-v1.p.rapidapi.com";
    private final String baseUrl = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v3/get-profile";

    @Async("asyncExecutor")
    public CompletableFuture<String> getStockProfile(String symbol) throws IOException {

        Stock existingStock = stockRepository.findBySymbol(symbol);
        if (existingStock != null) {
            return CompletableFuture.completedFuture(existingStock.getProfileData());
        }

        AsyncHttpClient client = new DefaultAsyncHttpClient();
        String url = baseUrl + "?symbol=" + symbol + "&region=US&lang=en-US";

        // Making the API call asynchronously
        CompletableFuture<String> result = client.prepare("GET", url)
             .setHeader("x-rapidapi-key", apiKey)
             .setHeader("x-rapidapi-host", apiHost)
              .execute()
              .toCompletableFuture()
              .thenApply(response -> {
                    String responseBody = response.getResponseBody();

                    // Save the fetched stock profile to the database
                    Stock stock = new Stock(symbol, responseBody);
                    stockRepository.save(stock);

                    return responseBody;
                    })
              .exceptionally(ex -> {
                    // Handle any exceptions here
                    return "Error: " + ex.getMessage();
              });
        result.whenComplete((res, ex) -> {
                try {
                    client.close();
                } catch (IOException e) {
                    // Log or handle the exception if closing fails
                    e.printStackTrace();
                }
        });
        return result;
    }
}
