package com.allobank.idr_rate_aggregator.runner;

import com.allobank.idr_rate_aggregator.service.IDRDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializerRunner implements ApplicationRunner{
    private static final Logger logger = LoggerFactory.getLogger(DataInitializerRunner.class);

    private final IDRDataService idrDataService;

    public DataInitializerRunner(IDRDataService idrDataService) {
        this.idrDataService = idrDataService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("========================================");
        logger.info("Starting Data Initialization from Frankfurter API...");
        logger.info("========================================");

        try {
            idrDataService.loadInitialData();
            logger.info("========================================");
            logger.info("Data Initialization Completed Successfully!");
            logger.info("========================================");
        } catch (Exception e) {
            logger.error("========================================");
            logger.error("Data Initialization FAILED!");
            logger.error("Error: {}", e.getMessage());
            logger.error("========================================");
            throw e;
        }
    }
}
