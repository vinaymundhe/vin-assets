package com.va.vinassets.models;

import jakarta.persistence.*;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    @Lob  // This annotation allows large text data
    @Column(length = 100000)  // Optional: Set an explicit length if needed
    private String profileData; // Store JSON response or parsed fields

    public Stock(String symbol, String profileData) {
        this.symbol = symbol;
        this.profileData = profileData;
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
}


