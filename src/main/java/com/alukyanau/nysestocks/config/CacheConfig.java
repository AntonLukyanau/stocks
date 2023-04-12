package com.alukyanau.nysestocks.config;

import com.alukyanau.nysestocks.entity.RequestToNYSE;
import com.alukyanau.nysestocks.entity.StockData;
import com.alukyanau.nysestocks.infrastructure.cache.RequestCache;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.repository.RequestToNYSERepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Log4j2
public class CacheConfig {

    @Value("${stocks.request.cache.size}")
    private Integer cacheSize;

    @Bean
    public RequestCache<RequestParameters, List<StockData>> requestCache(RequestToNYSERepository requestRepository) {
        RequestCache<RequestParameters, List<StockData>> requestCache = new RequestCache<>(cacheSize);
        List<RequestToNYSE> lastRequests = requestRepository.findLastRequests(cacheSize);
        for (RequestToNYSE request : lastRequests) {
            RequestParameters parameters = new RequestParameters(
                    request.getCompanyParameter(),
                    request.getStartDateParameter(),
                    request.getEndDateParameter()
            );
            requestCache.store(parameters, request.getStocks());
        }
        log.debug("Cache has {} started size", requestCache.size());
        return requestCache;
    }

}
