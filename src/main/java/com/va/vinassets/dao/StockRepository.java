package com.va.vinassets.dao;

import com.va.vinassets.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    // Custom query methods can be added here if needed
    Stock findBySymbol(String symbol);
    List<Stock> findAll(); // To retrieve all stocks in the portfolio
}

