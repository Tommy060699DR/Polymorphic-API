package com.allobank.idr_rate_aggregator.service;

import com.allobank.idr_rate_aggregator.dto.UnifiedResponse;
import com.allobank.idr_rate_aggregator.exception.ResourceNotFoundException;
import com.allobank.idr_rate_aggregator.usecase.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IDRDataService {
    private static final Logger logger = LoggerFactory.getLogger(IDRDataService.class);

    private final Map<String, IDRDataFetcher> strategyMap;

    private final Map<String, UnifiedResponse> dataStore;

    public IDRDataService(List<IDRDataFetcher> fetchers) {
        this.strategyMap = new ConcurrentHashMap<>();
        this.dataStore = new ConcurrentHashMap<>();


        for (IDRDataFetcher fetcher : fetchers) {
            strategyMap.put(fetcher.getResourceType(), fetcher);
            logger.info("Registered strategy: {}", fetcher.getResourceType());
        }
    }

    public void loadInitialData() {
        logger.info("Starting initial data loading...");

        for (IDRDataFetcher fetcher : strategyMap.values()) {
            try {
                UnifiedResponse response = fetcher.fetchData();
                dataStore.put(fetcher.getResourceType(), response);
                logger.info("Successfully loaded data for: {}", fetcher.getResourceType());
            } catch (Exception e) {
                logger.error("Failed to load data for: {} - Error: {}",
                        fetcher.getResourceType(), e.getMessage());

                dataStore.put(fetcher.getResourceType(),
                        new UnifiedResponse(
                                fetcher.getResourceType(),
                                new ArrayList<>(),
                                "Failed to load data: " + e.getMessage()
                        ));
            }
        }

        logger.info("Initial data loading completed. Total resources: {}", dataStore.size());
    }

    public UnifiedResponse getData(String resourceType) {
        logger.info("Fetching data for resource type: {}", resourceType);
        
        if (!strategyMap.containsKey(resourceType)) {
            logger.warn("Resource type not found: {}", resourceType);
            throw new ResourceNotFoundException(resourceType);
        }

        UnifiedResponse response = dataStore.get(resourceType);

        if (response == null) {
            logger.error("Data not loaded for resource type: {}", resourceType);
            throw new ResourceNotFoundException(resourceType, "Data not loaded yet");
        }

        logger.info("Successfully retrieved data for {}: {} records", 
                resourceType, response.getData().size());
        return response;
    }


    public List<String> getAvailableResourceTypes() {
        List<String> resources = new ArrayList<>(strategyMap.keySet());
        logger.info("Returning {} available resource types", resources.size());
        return resources;
    }
}
