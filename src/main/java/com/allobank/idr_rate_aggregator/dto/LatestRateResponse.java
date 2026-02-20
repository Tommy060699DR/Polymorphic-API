package com.allobank.idr_rate_aggregator.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestRateResponse {
    @JsonProperty("amount")
    private String amount;

    @JsonProperty("base")
    private String base;

    @JsonProperty("date")
    private String date;

    private Map<String, Double> rates;
}
