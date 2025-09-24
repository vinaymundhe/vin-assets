package com.va.vinassets.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Top-level dashboard view for the user.
 * Contains lightweight user info and the full portfolio snapshot.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DashboardDTO(
        UserSummaryDTO user,
        MyPortfolioDTO portfolio,
        Instant lastUpdated // when this snapshot was computed
) {}
