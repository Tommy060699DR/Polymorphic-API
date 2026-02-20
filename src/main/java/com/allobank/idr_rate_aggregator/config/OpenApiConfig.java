package com.allobank.idr_rate_aggregator.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Allo Bank IDR Rate Aggregator API")
                        .version("1.0.0")
                        .description("""
                                ## Polymorphic API untuk Data Kurs IDR
                                
                                Aplikasi ini mengintegrasikan data dari Frankfurter API 
                                dengan fitur kalkulasi spread banking unik.
                                
                                ### Fitur Utama:
                                - **latest_idr_rates**: Kurs terbaru IDR dengan kalkulasi spread banking
                                - **historical_idr_usd**: Data historis IDR ke USD (2024-01-01 s/d 2024-01-05)
                                - **supported_currencies**: Daftar mata uang yang didukung
                                
                                ### Spread Factor Calculation:
                                - Username: developer
                                - Sum Unicode: 1066
                                - Spread Factor: 0.00066
                                """)
                        .contact(new Contact()
                                .name("Allo Bank Backend Team")
                                .email("backend@allobank.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development")
                ));
    }
}
