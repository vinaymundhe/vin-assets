package com.va.vinassets.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    private String symbol;
    private double quantity;
    private double purchasePrice;
    private LocalDate purchaseDate;
    private double pnL;

    public double getPnL() {
        return pnL;
    }

    public void setPnL(double pnL) {
        this.pnL = pnL;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
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

    @Override
    public String toString() {
        return "{" +
                "symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                ", purchasePrice=" + purchasePrice +
                ", purchaseDate=" + purchaseDate +
                ", profitAndLoss=" + pnL +
                '}';
    }
}