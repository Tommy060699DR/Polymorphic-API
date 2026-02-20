package com.allobank.idr_rate_aggregator.usecase;

import com.allobank.idr_rate_aggregator.dto.UnifiedResponse;

public interface IDRDataFetcher {
    /**
     * Mengembalikan tipe resource yang ditangani
     */
    String getResourceType();

    /**
     * Mengambil dan memproses data dari external API
     */
    UnifiedResponse fetchData();
}
