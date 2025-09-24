package com.va.vinassets.dto;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A single stock holding (position) in the user's portfolio.
 * Computed fields (currentPrice/currentValue/pnl...) can be null if not requested.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StockPositionDTO(
        String symbol,
        BigDecimal quantity,
        BigDecimal averagePrice,
        // optional, filled by price service at read time
        BigDecimal currentPrice,
        BigDecimal currentValue,
        BigDecimal invested,
        BigDecimal profitAndLoss,
        BigDecimal pnlPercent,
        List<TradeBreakdownDTO> breakdowns
) {}
