package com.va.vinassets.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    @Lob  // This annotation allows large text data
    @Column(columnDefinition = "LONGTEXT")  // Optional: Set an explicit length if needed
    private String profileData; // Store JSON response or parsed fields
    private int quantity; // New field to store how much stock user bought
    private double purchasePrice; // New field to store the purchase price per share
    private LocalDate purchaseDate; // New field to store the purchase date

    @Transient
    private double currentValue; // Not stored in the database but calculated using current stock price

    @Transient
    private double pnl; // Profit & Loss, also transient and not stored

    public Stock() {
    }

    public Stock(String symbol, String profileData) {
        this.symbol = symbol;
        this.profileData = profileData;
    }

    // New constructor to add stock with additional fields
    public Stock(String symbol, String profileData, int quantity, double purchasePrice, LocalDate purchaseDate) {
        this.symbol = symbol;
        this.profileData = profileData;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
    }

    public Stock(String symbol, Object profileData, int quantity, double purchasePrice, LocalDate purchaseDate) {
    }

    // Getters and Setters
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

    public String getProfileData() {
        return profileData;
    }

    public void setProfileData(String profileData) {
        this.profileData = profileData;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getPnl() {
        return pnl;
    }

    public void setPnl(double pnl) {
        this.pnl = pnl;
    }

    public double calculatePnl(double currentStockPrice) {
        this.currentValue = this.quantity * currentStockPrice;
        this.pnl = this.currentValue - (this.quantity * this.purchasePrice);
        return this.pnl;
    }
}


