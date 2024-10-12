package com.va.vinassets.models;

import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;  // Stock symbol (e.g., AAPL, MSFT)

    @Lob
    @Column(columnDefinition = "LONGTEXT")  // Optional: Set an explicit length if needed
    private String profileData;  // Stores stock profile data fetched from an external API

    public Stock() {
    }

    public Stock(String symbol, String profileData) {
        this.symbol = symbol;
        this.profileData = profileData;
    }

    public Stock(String symbol) {
    }

    // Getters and setters
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
}
