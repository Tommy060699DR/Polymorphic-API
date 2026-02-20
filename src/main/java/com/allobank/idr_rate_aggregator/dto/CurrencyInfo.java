package com.allobank.idr_rate_aggregator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Currency information")
public class CurrencyInfo {
    @Schema(description = "Currency code", example = "USD")
    private String code;

    @Schema(description = "Currency name", example = "United States Dollar")
    private String name;
}
