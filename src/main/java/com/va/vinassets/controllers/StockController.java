package com.va.vinassets.controllers;

import com.va.vinassets.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/{symbol}")
    public CompletableFuture<String> getStockProfile(@PathVariable String symbol) throws IOException {
        return stockService.getStockProfile(symbol);
    }
}
