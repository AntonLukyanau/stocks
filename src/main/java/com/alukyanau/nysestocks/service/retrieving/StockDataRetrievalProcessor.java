package com.alukyanau.nysestocks.service.retrieving;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.entity.RequestToNYSE;
import com.alukyanau.nysestocks.entity.StockData;
import com.alukyanau.nysestocks.infrastructure.cache.RequestCache;
import com.alukyanau.nysestocks.infrastructure.cache.RequestCacheSupportable;
import com.alukyanau.nysestocks.model.CSVStockData;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.repository.RequestToNYSERepository;
import com.alukyanau.nysestocks.repository.StockRepository;
import com.alukyanau.nysestocks.service.DataRetrievalProcessor;
import com.alukyanau.nysestocks.service.csv.DataRetriever;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class StockDataRetrievalProcessor implements DataRetrievalProcessor<List<StockDataDTO>>, RequestCacheSupportable {

    private final DataRetriever<List<CSVStockData>> stockDataRetriever;
    private final Converter<StockData, StockDataDTO> stockDataConverter;

    private final RequestToNYSERepository requestRepository;
    private final StockRepository stockRepository;
    private final RequestCache<RequestParameters, List<StockData>> requestCache;

    @Override
    @Transactional
    public List<StockDataDTO> retrievalProcess(RequestParameters parameters) {
        if (parameters.isContainsNull()) {
            log.warn("Invoke retrieval process with missing parameters. Parameters: " + parameters);
            return Collections.emptyList();
        }
        if (requestCache.containsKey(parameters)) {
            return convertToDTO(requestCache.getBy(parameters));
        }
        List<CSVStockData> retrievedData = stockDataRetriever.retrieveData(parameters);

        RequestToNYSE request = RequestToNYSE.builder()
                .companyParameter(parameters.companyName())
                .startDateParameter(parameters.startDate())
                .endDateParameter(parameters.endDate())
                .date(LocalDate.now())
                .build();
        List<StockData> stocksData = retrievedData.stream()
                .map(csvStockData -> StockData.builder()
                        .request(request)
                        .companyCode(parameters.companyName())
                        .date(csvStockData.getDate())
                        .startPrice(csvStockData.getOpen())
                        .maxPrice(csvStockData.getHigh())
                        .minPrice(csvStockData.getLow())
                        .endPrice(csvStockData.getClose())
                        .volume(csvStockData.getVolume())
                        .build())
                .toList();

        if (stocksData.isEmpty()) {
            log.warn("Retrieve data from NYSE was failed. Parameters: " + parameters);
        } else {
            saveRequestResults(request, stocksData);
            requestCache.store(parameters, stocksData);
        }
        return convertToDTO(stocksData);
    }

    private List<StockDataDTO> convertToDTO(List<StockData> stocksData) {
        return stocksData.stream()
                .map(stockDataConverter::convert)
                .toList();
    }

    private void saveRequestResults(RequestToNYSE request, List<StockData> stocksData) {
        List<StockData> stocks = List.copyOf(stocksData);
        request.setStocks(stocks);
        requestRepository.save(request);
        stocks.forEach(stockData -> stockData.setRequest(request));
        stockRepository.saveAll(stocks);
    }

}
