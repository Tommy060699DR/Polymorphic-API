package com.allobank.idr_rate_aggregator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response unified untuk semua tipe resource")
public class UnifiedResponse {
    @Schema(description = "Tipe resource yang diminta", example = "latest_idr_rates")
    private String resourceType;

    @Schema(description = "Data hasil response")
    private List<Object> data;

    @Schema(description = "Pesan error jika ada", example = "null")
    private String error;

    public UnifiedResponse(String resourceType, List<Object> data) {
        this.resourceType = resourceType;
        this.data = data;
    }
}
