package com.va.vinassets.services;

import com.va.vinassets.dao.PortfolioRepository;
import com.va.vinassets.models.Breakdown;
import com.va.vinassets.models.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioServiceTest {

    @InjectMocks
    private PortfolioService portfolioService;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper methods to create mock Portfolio and Breakdown
    private Portfolio createPortfolio(String symbol, double qty, double price) {
        Portfolio p = new Portfolio();
        p.setSymbol(symbol);
        p.setQuantity(qty);
        p.setAveragePrice(price);
        p.setBreakdownList(new ArrayList<>());
        return p;
    }

    @Test
    void testAddStockToPortfolio_NewStock() {
        String symbol = "TATA";
        double qty = 10;
        double price = 100;
        LocalDate purchaseDate = LocalDate.now().minusDays(5);

        when(portfolioRepository.findBySymbol(symbol)).thenReturn(null);
        when(stockService.getCurrentStockPrice(symbol)).thenReturn(CompletableFuture.completedFuture(120.0));

        String result = portfolioService.addStockToPortfolio(symbol, qty, price, purchaseDate);

        ArgumentCaptor<Portfolio> portfolioCaptor = ArgumentCaptor.forClass(Portfolio.class);
        verify(portfolioRepository).save(portfolioCaptor.capture());
        Portfolio savedPortfolio = portfolioCaptor.getValue();

        assertEquals("Added new stock to portfolio.", result);
        assertEquals(symbol, savedPortfolio.getSymbol());
        assertEquals(qty, savedPortfolio.getQuantity());
        assertEquals(price, savedPortfolio.getAveragePrice());
        assertEquals(1, savedPortfolio.getBreakdownList().size());
        assertEquals(20.0, savedPortfolio.getBreakdownList().get(0).getPnLSinceBuyPrice());
    }

    @Test
    void testAddStockToPortfolio_ExistingStock() {
        String symbol = "RELIANCE";
        double prevQty = 5, addQty = 10;
        double prevPrice = 200, newPrice = 220;
        LocalDate purchaseDate = LocalDate.now().minusDays(10);

        Portfolio existing = createPortfolio(symbol, prevQty, prevPrice);
        when(portfolioRepository.findBySymbol(symbol)).thenReturn(existing);
        when(stockService.getCurrentStockPrice(symbol)).thenReturn(CompletableFuture.completedFuture(210.0));

        String result = portfolioService.addStockToPortfolio(symbol, addQty, newPrice, purchaseDate);

        assertEquals("Updated existing stock in portfolio.", result);
        assertEquals(prevQty + addQty, existing.getQuantity());

        // Weighted avg price calculation
        double weightedAvg = ((prevPrice * prevQty) + (newPrice * addQty)) / (prevQty + addQty);
        assertEquals(weightedAvg, existing.getAveragePrice());

        verify(portfolioRepository).save(existing);
        assertFalse(existing.getBreakdownList().isEmpty());
    }

    @Test
    void testAddStockToPortfolio_NullPurchaseDate_ShouldThrow() {
        String symbol = "INFY";
        double qty = 1;
        double price = 10;

        when(portfolioRepository.findBySymbol(symbol)).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                portfolioService.addStockToPortfolio(symbol, qty, price, null)
        );
        assertEquals("Purchase date cannot be null", ex.getMessage());
    }

    @Test
    void testDeleteStockFromPortfolio_Found() {
        String symbol = "HDFCBANK";
        Portfolio toDelete = createPortfolio(symbol, 2, 50);
        when(portfolioRepository.findBySymbol(symbol)).thenReturn(toDelete);

        String result = portfolioService.deleteStockFromPortfolio(symbol);

        verify(portfolioRepository).delete(toDelete);
        assertEquals("Deleted HDFCBANK", result);
    }

    @Test
    void testDeleteStockFromPortfolio_NotFound() {
        String symbol = "MISSING";
        when(portfolioRepository.findBySymbol(symbol)).thenReturn(null);

        // Should not throw, but will cause NullPointerException on getSymbol in your code!
        // Let's check for that bug:
        assertThrows(NullPointerException.class, () ->
                portfolioService.deleteStockFromPortfolio(symbol)
        );
    }

    @Test
    void testGetCompletePortfolio() {
        Portfolio p1 = createPortfolio("TCS", 1, 10);
        Portfolio p2 = createPortfolio("ITC", 2, 20);

        List<Portfolio> portfolios = Arrays.asList(p1, p2);

        when(portfolioRepository.findAll()).thenReturn(portfolios);
        when(stockService.getCurrentStockPrice(anyString())).thenReturn(CompletableFuture.completedFuture(30.0));

        List<Portfolio> result = portfolioService.getCompletePortfolio();

        assertEquals(2, result.size());
        for (Portfolio p : result) {
            assertEquals(30.0, p.getCurrentPrice());
            // Calculate expected profit and loss
            double expectedPnl = (30.0 * p.getQuantity()) - (p.getAveragePrice() * p.getQuantity());
            assertEquals(expectedPnl, p.getProfitAndLoss());
        }
    }

    @Test
    void testGetStockBreakdown() {
        Portfolio p = createPortfolio("DMART", 3, 100);
        when(portfolioRepository.findBySymbol("DMART")).thenReturn(p);
        when(stockService.getCurrentStockPrice("DMART")).thenReturn(CompletableFuture.completedFuture(120.0));

        Portfolio result = portfolioService.getStockBreakdown("DMART");

        assertEquals(p, result);
        assertEquals(120.0, result.getCurrentPrice());
        assertEquals(60.0, result.getProfitAndLoss()); // (120*3) - (100*3)
    }

}
