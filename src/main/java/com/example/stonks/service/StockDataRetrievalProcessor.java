package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.entity.RequestToNYSE;
import com.example.stonks.entity.StockData;
import com.example.stonks.repository.RequestToNYSERepository;
import com.example.stonks.service.cache.RequestCache;
import com.example.stonks.service.rest.DataRetriever;
import com.example.stonks.util.RequestParameters;
import com.example.stonks.util.StockDataWrap;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

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
    private final RequestCache<RequestParameters, List<StockDataDTO>> requestCache;

    @Override
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
        requestCache.store(parameters, List.copyOf(stockDataDTOS));
    }

}
