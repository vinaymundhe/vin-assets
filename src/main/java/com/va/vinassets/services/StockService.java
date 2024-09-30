package com.va.vinassets.services;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Service
public class StockService {

        private final String apiKey = "00789db411mshaf17852c311cc33p1bee58jsnc42f3f06271e";  // Use your API key here
        private final String apiHost = "apidojo-yahoo-finance-v1.p.rapidapi.com";
        private final String baseUrl = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v3/get-profile";

        @Async("asyncExecutor")
        public CompletableFuture<String> getStockProfile(String symbol) throws IOException {
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            String url = baseUrl + "?symbol=" + symbol + "&region=US&lang=en-US";

            // Making the API call asynchronously
            CompletableFuture<String> result = client.prepare("GET", url)
                    .setHeader("x-rapidapi-key", apiKey)
                    .setHeader("x-rapidapi-host", apiHost)
                    .execute()
                    .toCompletableFuture()
                    .thenApply(response -> {
                        // Parse or return the response as needed
                        return response.getResponseBody();
                    })
                    .exceptionally(ex -> {
                        // Handle any exceptions here
                        return "Error: " + ex.getMessage();
                    });

            client.close();  // Close the client after use
            return result;
        }
    }
