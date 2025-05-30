package com.va.vinassets.models;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public class Breakdown {

    private LocalDate transactionDate;
    private double quantity;
    private double price;
    private long daysFromTransaction;
    private double pnLSinceBuyPrice;

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getDaysFromTransaction() {
        return daysFromTransaction;
    }

    public void setDaysFromTransaction(long daysFromTransaction) {
        this.daysFromTransaction = daysFromTransaction;
    }

    public double getPnLSinceBuyPrice() {
        return pnLSinceBuyPrice;
    }

    public void setPnLSinceBuyPrice(double pnLSinceBuyPrice) {
        this.pnLSinceBuyPrice = pnLSinceBuyPrice;
    }
}
