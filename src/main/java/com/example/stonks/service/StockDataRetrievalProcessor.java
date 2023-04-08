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
        String companyName = parameters.companyName();
        NYSEResultFrequency frequency = parameters.frequency();
        LocalDate startDate = parameters.startDate();
        LocalDate endDate = parameters.endDate();
        if (companyName == null || frequency == null) {
            log.warn("Invoke retrieval process with missing parameters. Parameters: " + parameters);
            return Collections.emptyList();
        }
        String data = stockDataRetriever.retrieveData(parameters);

        StockDataWrap dataWrapper = new StockDataWrap(data, companyName, frequency);
        List<StockDataDTO> stockDataDTOS = stockDataParser.parse(dataWrapper);
        if (stockDataDTOS.isEmpty()) {
            log.warn("Retrieve data from NYSE was failed. Parameters: " + parameters);
        } else {
            List<StockData> stocks = stockDataDTOS.stream()
                    .map(stockDataConverter::convert)
                    .filter(Objects::nonNull)
                    .toList();
            RequestToNYSE request = RequestToNYSE.builder()
                    .companyParameter(companyName)
                    .frequencyParameter(frequency)
                    .startDateParameter(startDate)
                    .endDateParameter(endDate)
                    .date(LocalDate.now())
                    .stocks(stocks)
                    .build();
            requestRepository.save(request);
        }
        return stockDataDTOS;
    }

}
