package com.va.vinassets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * One trade/lot that contributes to a position.
 * Note: daysFromTransaction and pnlSinceBuyPrice are computed on read.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TradeBreakdownDTO(
        LocalDate transactionDate,
        BigDecimal quantity,
        BigDecimal price,
        Long daysFromTransaction,
        BigDecimal pnlSinceBuyPrice
) {}

