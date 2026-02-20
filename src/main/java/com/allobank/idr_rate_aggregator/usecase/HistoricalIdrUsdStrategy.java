package com.allobank.idr_rate_aggregator.usecase;

import com.allobank.idr_rate_aggregator.dto.HistoricalRateResponse;
import com.allobank.idr_rate_aggregator.dto.UnifiedResponse;
import com.allobank.idr_rate_aggregator.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class HistoricalIdrUsdStrategy implements IDRDataFetcher{
    private static final Logger logger = LoggerFactory.getLogger(HistoricalIdrUsdStrategy.class);

    private final WebClient webClient;

    public HistoricalIdrUsdStrategy(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public UnifiedResponse fetchData() {
        logger.info("Fetching historical IDR to USD rates...");


        String startDate = "2025-12-01";
        String endDate = "2026-02-05";

        // Validasi format tanggal
        LocalDate start = DateUtils.parseDate(startDate);
        LocalDate end = DateUtils.parseDate(endDate);

        logger.debug("Date range: {} to {}", start, end);

        HistoricalRateResponse response = webClient.get()
                .uri("/{start}..{end}?from=IDR&to=USD", startDate, endDate)
                .retrieve()
                .bodyToMono(HistoricalRateResponse.class)
                .block();

        logger.info("API Response: {}", response);

        List<Object> results = new ArrayList<>();

        if (response != null && response.getRates() != null) {
            response.getRates().forEach((date, rates) -> {
                Map<String, Object> dayData = new LinkedHashMap<>();

                // Format tanggal menggunakan DateUtils
                String formattedDate = DateUtils.formatDate(DateUtils.parseDate(date));
                dayData.put("date", formattedDate);
                dayData.put("rates", rates);

                results.add(dayData);
            });
        }

        logger.info("Fetched {} historical records", results.size());

        return new UnifiedResponse(getResourceType(), results);
    }
}
