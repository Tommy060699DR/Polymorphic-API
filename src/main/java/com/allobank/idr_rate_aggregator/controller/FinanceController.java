package com.allobank.idr_rate_aggregator.controller;

import com.allobank.idr_rate_aggregator.dto.UnifiedResponse;
import com.allobank.idr_rate_aggregator.service.IDRDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@Tag(name = "Finance Data", description = "APIs untuk mengambil data kurs dan mata uang")
public class FinanceController {
    private static final Logger logger = LoggerFactory.getLogger(FinanceController.class);
    private final IDRDataService idrDataService;

    public FinanceController(IDRDataService idrDataService) {
        this.idrDataService = idrDataService;
    }

    @GetMapping("/data/{resourceType}")
    @Operation(
            summary = "Ambil Data Kurs/Mata Uang",
            description = """
                    Mengembalikan data berdasarkan tipe resource yang diminta.
                    
                    **Tipe Resource yang tersedia:**
                    - `latest_idr_rates` : Kurs terbaru IDR terhadap mata uang lain 
                      (dengan kalkulasi spread banking unik)
                    - `historical_idr_usd` : Data historis IDR ke USD 
                      (periode 2024-01-01 s/d 2024-01-05)
                    - `supported_currencies` : Daftar semua mata uang yang didukung 
                      Frankfurter API
                    """,
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UnifiedResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Resource Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Error Response",
                                            value = "{\"error\": \"Resource type not found\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<UnifiedResponse> getFinanceData(
            @Parameter(
                    description = "Tipe resource yang diinginkan",
                    required = true,
                    examples = {
                            @ExampleObject(name = "Latest IDR Rates", value = "latest_idr_rates"),
                            @ExampleObject(name = "Historical IDR USD", value = "historical_idr_usd"),
                            @ExampleObject(name = "Supported Currencies", value = "supported_currencies")
                    }
            )
            @PathVariable String resourceType) {

        UnifiedResponse response = idrDataService.getData(resourceType);

        if (response.getError() != null) {
            logger.warn("Resource not found: {}", resourceType);
            return ResponseEntity.notFound().build();
        }

        logger.info("Response for {}: {} records", resourceType, response.getData().size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/resources")
    @Operation(
            summary = "Daftar Resource yang Tersedia",
            description = "Mengembalikan daftar semua tipe resource yang tersedia"
    )
    public ResponseEntity<List<String>> getAvailableResources() {
        List<String> resources = idrDataService.getAvailableResourceTypes();
        logger.info("Available resources: {}", resources);
        return ResponseEntity.ok(resources);
    }
}
