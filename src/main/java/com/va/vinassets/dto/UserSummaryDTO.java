package com.va.vinassets.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Minimal user details for the dashboard header.
 * Keep this tiny; pull heavier stuff on demand.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserSummaryDTO(
        String userId,
        String name,
        String email
) {}

