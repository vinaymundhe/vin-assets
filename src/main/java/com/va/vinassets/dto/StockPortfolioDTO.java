package com.va.vinassets.dto;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A user's stock portfolio summary + list of positions.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StockPortfolioDTO(
        BigDecimal invested,       // total money put in
        BigDecimal currentValue,   // total value at current prices
        BigDecimal profitAndLoss,  // absolute P&L
        BigDecimal pnlPercent,     // percentage return
        List<StockPositionDTO> positions
) {}

