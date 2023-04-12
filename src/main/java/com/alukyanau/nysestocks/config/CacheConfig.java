package com.alukyanau.nysestocks.config;

import com.alukyanau.nysestocks.entity.StockData;
import com.alukyanau.nysestocks.infrastructure.cache.RequestCache;
import com.alukyanau.nysestocks.model.RequestParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CacheConfig {

    @Value("${stocks.request.cache.size}")
    private Integer cacheSize;

    @Bean
    public RequestCache<RequestParameters, List<StockData>> requestCache() {
        return new RequestCache<>(cacheSize);
    }

}
