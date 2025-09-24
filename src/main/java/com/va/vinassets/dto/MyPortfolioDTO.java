package com.va.vinassets.dto;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The user's full portfolio (stocks + crypto).
 * Aggregates both portfolios and shows overall totals.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MyPortfolioDTO(
        BigDecimal invested,         // total invested across all asset classes
        BigDecimal currentValue,     // total current value
        BigDecimal profitAndLoss,    // absolute overall P&L
        BigDecimal pnlPercent,       // overall % return
        StockPortfolioDTO stockPortfolio,
        CryptoPortfolioDTO cryptoPortfolio
) {}

