package com.va.vinassets.dao;

import com.va.vinassets.models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, String> {
    Portfolio findBySymbol(String symbol);
}


