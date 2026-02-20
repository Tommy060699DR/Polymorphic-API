package com.allobank.idr_rate_aggregator.usecase;

import com.allobank.idr_rate_aggregator.dto.LatestRateResponse;
import com.allobank.idr_rate_aggregator.dto.UnifiedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LatestIdrRatesStrategy implements IDRDataFetcher{
    private static final Logger logger = LoggerFactory.getLogger(LatestIdrRatesStrategy.class);

    private final WebClient webClient;

    @Value("${app.username}")
    private String githubUsername;

    public LatestIdrRatesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public UnifiedResponse fetchData() {
        logger.info("Fetching latest IDR rates...");

        LatestRateResponse response = webClient
                .get()
                .uri("/latest?from=IDR")
                .retrieve()
                .bodyToMono(LatestRateResponse.class)
                .block();

        logger.info("API Response: {}", response);

        List<Object> results = new ArrayList<>();

        if (response != null && response.getRates() != null) {
            Double rateUsd = response.getRates().get("USD");

            if (rateUsd == null || rateUsd == 0 || githubUsername == null) {
                logger.warn("Invalid data: rateUsd={}, githubUsername={}", rateUsd, githubUsername);
                return new UnifiedResponse(getResourceType(), results);
            }

            long sumUnicode = githubUsername.toLowerCase()
                    .chars()
                    .asLongStream()
                    .sum();

            double spreadFactor = (sumUnicode % 1000) / 100000.0;

            double rawIdrPerUsd = 1.0 / rateUsd;
            double spreadedIdrPerUsd = rawIdrPerUsd * (1 + spreadFactor);

            logger.info("Spread calculation - Username: {}, Sum: {}, Factor: {}",
                    githubUsername, sumUnicode, spreadFactor);

            results.add(Map.of(
                    "Base", "IDR",
                    "Date", response.getDate(),
                    "Rate_USD", rateUsd,
                    "Spread_Factor", spreadFactor,
                    "Calculated_Username_Sum", sumUnicode,
                    "USD_BuySpread_IDR", spreadedIdrPerUsd
            ));
        }

        return new UnifiedResponse(getResourceType(), results);
    }
}
