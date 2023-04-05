package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.entity.StockData;
import com.example.stonks.repository.StockRepository;
import com.example.stonks.service.rest.DataRetriever;
import com.example.stonks.util.NYSEConstants;
import com.example.stonks.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class StockDataRetrievalProcessor implements DataRetrievalProcessor<List<StockDataDTO>> {

    private final Parser<StockDataDTO, StockDataWrap> stockDataParser;
    private final DataRetriever<String> stockDataRetriever;
    private final Validator<String> csvValidator;
    private final Converter<StockDataDTO, StockData> stockDataConverter;
    private final StockRepository stockRepository;

    @Override
    public List<StockDataDTO> retrievalProcess(Map<String, Object> parameters) {
        String data = stockDataRetriever.retrieveData(parameters);
        if (!csvValidator.validate(data)) {
            log.warn("Retrieve data from NYSE was failed. Parameters: " + parameters);
            return Collections.emptyList();
        }
        String companyCode = String.valueOf(parameters.get(NYSEConstants.COMPANY_PARAMETER));
        NYSEResultFrequency frequency = (NYSEResultFrequency) parameters.get(NYSEConstants.FREQUENCY_PARAMETER);
        StockDataWrap dataWrapper = new StockDataWrap(data, companyCode, frequency);
        List<StockDataDTO> stockDataDTOS = stockDataParser.parse(dataWrapper);
        if (stockDataDTOS.isEmpty()) {
            log.warn("Retrieve data from NYSE was failed. Parameters: " + parameters);
        } else {
            List<StockData> stocks = stockDataDTOS.stream()
                    .map(stockDataConverter::convert)
                    .toList();
            stockRepository.saveAll(stocks);
        }
        return stockDataDTOS;
    }

}
