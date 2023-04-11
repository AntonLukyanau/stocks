package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.dto.NYSEResultFrequency;
import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.entity.RequestToNYSE;
import com.alukyanau.nysestocks.entity.StockData;
import com.alukyanau.nysestocks.repository.RequestToNYSERepository;
import com.alukyanau.nysestocks.repository.StockRepository;
import com.alukyanau.nysestocks.service.cache.RequestCache;
import com.alukyanau.nysestocks.service.rest.DataRetriever;
import com.alukyanau.nysestocks.util.RequestParameters;
import com.alukyanau.nysestocks.util.StockDataWrap;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class StockDataRetrievalProcessor implements DataRetrievalProcessor<List<StockDataDTO>> {

    private final Parser<StockDataDTO, StockDataWrap> stockDataParser;
    private final DataRetriever<String> stockDataRetriever;
    private final Converter<StockDataDTO, StockData> stockDataConverter;
    private final RequestToNYSERepository requestRepository;
    private final StockRepository stockRepository;
    private final RequestCache<RequestParameters, List<StockDataDTO>> requestCache;

    @PostConstruct
    private void fillCache() {
        List<RequestToNYSE> lastRequests = requestRepository.findLastRequests(requestCache.getMaxSize());
        lastRequests.forEach(requestToNYSE -> {
            RequestParameters parameters = new RequestParameters(
                    requestToNYSE.getCompanyParameter(),
                    requestToNYSE.getFrequencyParameter(),
                    requestToNYSE.getStartDateParameter(),
                    requestToNYSE.getEndDateParameter()
            );
            List<StockDataDTO> results = requestToNYSE.getStocks().stream()
                    .map(stockData -> StockDataDTO.builder()
                            .companyCode(stockData.getCompanyCode())
                            .date(stockData.getDate())
                            .startPrice(stockData.getStartPrice())
                            .maxPrice(stockData.getMaxPrice())
                            .minPrice(stockData.getMinPrice())
                            .endPrice(stockData.getEndPrice())
                            .resultFrequency(stockData.getResultFrequency())
                            .volume(stockData.getVolume())
                            .build())
                    .toList();
            requestCache.store(parameters, results);
        });
        log.debug("Cache has {} started size", requestCache.size());
    }

    @Override
    @Transactional
    public List<StockDataDTO> retrievalProcess(RequestParameters parameters) {
        if (requestCache.containsKey(parameters)) {
            return requestCache.getBy(parameters);
        }
        String companyName = parameters.companyName();
        NYSEResultFrequency frequency = parameters.frequency();
        if (parameters.isContainsNull()) {
            log.warn("Invoke retrieval process with missing parameters. Parameters: " + parameters);
            return Collections.emptyList();
        }
        String data = stockDataRetriever.retrieveData(parameters);

        StockDataWrap dataWrapper = new StockDataWrap(data, companyName, frequency);
        List<StockDataDTO> stockDataDTOS = stockDataParser.parse(dataWrapper);
        if (stockDataDTOS.isEmpty()) {
            log.warn("Retrieve data from NYSE was failed. Parameters: " + parameters);
        } else {
            saveRequestResults(parameters, stockDataDTOS);
        }
        return stockDataDTOS;
    }

    private void saveRequestResults(RequestParameters parameters, List<StockDataDTO> stockDataDTOS) {
        List<StockData> stocks = stockDataDTOS.stream()
                .map(stockDataConverter::convert)
                .filter(Objects::nonNull)
                .toList();
        RequestToNYSE request = RequestToNYSE.builder()
                .companyParameter(parameters.companyName())
                .frequencyParameter(parameters.frequency())
                .startDateParameter(parameters.startDate())
                .endDateParameter(parameters.endDate())
                .date(LocalDate.now())
                .stocks(stocks)
                .build();
        requestRepository.save(request);
        stocks.forEach(stockData -> stockData.setRequest(request));
        stockRepository.saveAll(stocks);
        requestCache.store(parameters, List.copyOf(stockDataDTOS));
    }

}
