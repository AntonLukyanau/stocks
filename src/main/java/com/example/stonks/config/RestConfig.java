package com.example.stonks.config;

import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.service.cache.RequestCache;
import com.example.stonks.util.RequestParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestConfig {

    @Value("${stonks.request.cache.size}")
    private Integer cacheSize;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RequestCache<RequestParameters, List<StockDataDTO>> requestCache() {
        return new RequestCache<>(cacheSize);
    }

}
