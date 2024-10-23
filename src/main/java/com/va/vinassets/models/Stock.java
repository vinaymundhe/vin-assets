package com.va.vinassets.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;  // Stock symbol (e.g., AAPL, MSFT)

    @Column(nullable = true)
    private String companyName;  // Current company name

    @Column(name = "current_price", nullable = false)
    private Double currentPrice;

    @Column(nullable = true)
    private String currency;  // Currency of the stock

    public Stock() {
    }

    public Stock(Long id, String symbol, String companyName, Double currentPrice, String currency) {
        this.id = id;
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.currency = currency;
    }

    public Stock(String symbol) {
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        if (currentPrice < 0) {
            throw new IllegalArgumentException("Current price cannot be negative");
        }
        this.currentPrice = currentPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", currentPrice=" + currentPrice +
                ", currency='" + currency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id) && Objects.equals(symbol, stock.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol);
    }
}
