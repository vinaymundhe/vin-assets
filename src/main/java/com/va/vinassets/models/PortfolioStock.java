package com.va.vinassets.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;

/*
@Entity
@Table(name = "portfolio_stock")
public class PortfolioStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to Stock entity with cascade persist
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "stock_id")
    private Stock stock;

    // Newly added foreign key to Portfolio entity
    @ManyToOne
    @JoinColumn(name = "portfolio_id")  // This links to Portfolio's id
    @JsonBackReference // Indicates that this is the child in the relationship
    private Portfolio portfolio;

    private int quantity;
    private double purchasePrice;
    private LocalDate purchaseDate;

    // Constructor
    public PortfolioStock() {}

    public PortfolioStock(Stock stock, int quantity, double purchasePrice, LocalDate purchaseDate) {
        this.stock = stock;
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
*/