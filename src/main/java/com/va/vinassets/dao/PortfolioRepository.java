package com.va.vinassets.dao;

import com.va.vinassets.models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {
    List<Portfolio> findBySymbol(String symbol);
}


