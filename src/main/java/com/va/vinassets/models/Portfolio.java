package com.va.vinassets.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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

}