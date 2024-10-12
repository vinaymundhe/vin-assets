package com.va.vinassets.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;  // Assuming you are tracking portfolios by user

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference // Indicates that this is the parent in the relationship
    private List<PortfolioStock> portfolioStocks = new ArrayList<>();

    private double totalPnL; // Total Profit & Loss
    private double investedValue; // Total amount invested in the portfolio

    // Add PortfolioStock to portfolio
    public void addPortfolioStock(PortfolioStock portfolioStock) {
        portfolioStocks.add(portfolioStock);
        portfolioStock.setPortfolio(this);  // Set the foreign key relationship
    }

    // Remove PortfolioStock
    public void removePortfolioStock(PortfolioStock portfolioStock) {
        portfolioStocks.remove(portfolioStock);
        portfolioStock.setPortfolio(null);
    }

    public Portfolio() {
        this.portfolioStocks = new ArrayList<>();
    }

    public Portfolio(String userId) {
        this.userId = userId;
        this.portfolioStocks = new ArrayList<>();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<PortfolioStock> getPortfolioStocks() {
        return portfolioStocks;
    }

    public void setPortfolioStocks(List<PortfolioStock> portfolioStocks) {
        this.portfolioStocks = portfolioStocks;
    }

    public double getTotalPnL() {
        return totalPnL;
    }

    public void setTotalPnL(double totalPnL) {
        this.totalPnL = totalPnL;
    }

    public double getInvestedValue() {
        return investedValue;
    }

    public void setInvestedValue(double investedValue) {
        this.investedValue = investedValue;
    }
}
