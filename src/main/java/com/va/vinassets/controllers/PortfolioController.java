package com.va.vinassets.controllers;

// PortfolioController.java
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @GetMapping("/{userId}")
    public Portfolio getPortfolio(@PathVariable String userId) {
        // Mock data: In reality, this data comes from a database
        List<Investment> stocks = List.of(
                new Investment("AAPL", 10, 150),
                new Investment("GOOGL", 5, 2800)
        );
        List<Investment> cryptos = List.of(
                new Investment("bitcoin", 0.5, 40000),
                new Investment("ethereum", 2, 3000)
        );

        return new Portfolio(stocks, cryptos);
    }
}

