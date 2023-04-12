package com.alukyanau.nysestocks.infrastructure.cache;

import com.alukyanau.nysestocks.entity.RequestToNYSE;
import com.alukyanau.nysestocks.entity.StockData;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.repository.RequestToNYSERepository;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface RequestCacheSupportable {

    @Autowired
    default void fillOnStartUp(RequestToNYSERepository requestRepository, RequestCache<RequestParameters, List<StockData>> requestCache) {
        Logger log = org.apache.logging.log4j.LogManager.getLogger(this.getClass());
        List<RequestToNYSE> lastRequests = requestRepository.findLastRequests(requestCache.getMaxSize());
        lastRequests.forEach(requestToNYSE -> {
            RequestParameters parameters = new RequestParameters(
                    requestToNYSE.getCompanyParameter(),
                    requestToNYSE.getStartDateParameter(),
                    requestToNYSE.getEndDateParameter()
            );
            requestCache.store(parameters, requestToNYSE.getStocks());
        });
        log.debug("Cache has {} started size", requestCache.size());
    }

}
