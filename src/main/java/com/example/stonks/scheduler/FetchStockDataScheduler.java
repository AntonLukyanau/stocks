package com.example.stonks.scheduler;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.entity.StockData;
import com.example.stonks.repository.StockRepository;
import com.example.stonks.service.DataRetrievalProcessor;
import com.example.stonks.util.NYSEConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class FetchStockDataScheduler {

    private final DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    private final Converter<StockDataDTO, StockData> stockDataConverter;
    private final StockRepository stockRepository;

    @Value("${stock.companies}")
    private List<String> companies;
    @Value("${daily.fetch.retries}")
    private int retries;

    @Scheduled(cron = "0 0 0 * * *") // every day at 00:00
    public void dailyFetch() {
        NYSEResultFrequency frequency = NYSEResultFrequency.DAILY;
        for (String companyCode : companies) {
            doRequest(frequency, companyCode);
        }
    }

    private void doRequest(NYSEResultFrequency frequency, String companyCode) {
        Map<String, Object> parameters = Map.of(
                NYSEConstants.COMPANY_PARAMETER, companyCode,
                NYSEConstants.FREQUENCY_PARAMETER, frequency
        );
        for (int attempt = 0; attempt < retries; attempt++) {
            List<StockDataDTO> stockDataDTOS = stockDataRetrievalProcessor.retrievalProcess(parameters);
            if (!stockDataDTOS.isEmpty()) {
                List<StockData> stocks = stockDataDTOS.stream()
                        .map(stockDataConverter::convert)
                        .toList();
                stockRepository.saveAll(stocks);
                break;
            }
        }
    }

}
