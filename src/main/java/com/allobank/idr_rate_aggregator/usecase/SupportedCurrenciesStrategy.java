package com.allobank.idr_rate_aggregator.usecase;

import com.allobank.idr_rate_aggregator.dto.UnifiedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SupportedCurrenciesStrategy implements IDRDataFetcher {
    private static final Logger logger = LoggerFactory.getLogger(SupportedCurrenciesStrategy.class);

    private final WebClient webClient;

    public SupportedCurrenciesStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public UnifiedResponse fetchData() {
        logger.info("Fetching supported currencies...");
        Map<String, String> currencies = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        logger.info("API Response: {}", currencies);

        List<Object> results = new ArrayList<>();

        if (currencies != null) {
            currencies.forEach((code, name) -> {
                results.add(Map.of(
                        "code", code,
                        "name", name
                ));
            });
        }

        logger.info("Found {} supported currencies", results.size());

        return new UnifiedResponse(getResourceType(), results);
    }
}
