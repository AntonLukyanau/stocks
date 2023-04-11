package com.alukyanau.nysestocks.config;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.service.cache.RequestCache;
import com.alukyanau.nysestocks.util.RequestParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestConfig {

    @Value("${stocks.request.cache.size}")
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
