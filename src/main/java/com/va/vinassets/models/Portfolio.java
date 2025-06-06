package com.va.vinassets.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    private String symbol;
    private double quantity;
    private double averagePrice;
    private double currentPrice;
    private double invested;
    private double currentValue;
    private double profitAndLoss;
    @ElementCollection
    private List<Breakdown> breakdownList;

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

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getProfitAndLoss() {
        return profitAndLoss;
    }

    public void setProfitAndLoss(double profitAndLoss) {
        this.profitAndLoss = profitAndLoss;
    }

    public List<Breakdown> getBreakdownList() {
        return breakdownList;
    }

    public void setBreakdownList(List<Breakdown> breakdownList) {
        this.breakdownList = breakdownList;
    }

    public double getInvested() {
        return invested;
    }

    public void setInvested(double invested) {
        this.invested = invested;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
}