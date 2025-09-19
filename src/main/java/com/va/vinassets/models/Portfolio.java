package com.va.vinassets.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "portfolio")
@Getter
@Setter
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
    @CollectionTable(
            name = "portfolio_breakdown",
            joinColumns = @JoinColumn(name = "portfolio_symbol") // since our @Id is 'symbol'
    )
    private List<Breakdown> breakdownList;

}