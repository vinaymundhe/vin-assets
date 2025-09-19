package com.va.vinassets.models;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
public class Breakdown {

    private LocalDate transactionDate;
    private double quantity;
    private double price;
    private long daysFromTransaction;
    private double pnLSinceBuyPrice;

}
