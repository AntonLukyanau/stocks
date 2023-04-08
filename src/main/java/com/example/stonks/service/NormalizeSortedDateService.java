package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.dto.StockNormalizedDTO;
import com.example.stonks.util.NYSEConstants;
import com.example.stonks.util.RequestParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NormalizeSortedDateService implements NormalizeDateService {

    private final DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    private final Converter<StockDataDTO, StockNormalizedDTO> stockNormalizeDTOConverter;

    public List<StockNormalizedDTO> getNormalizedDTOS(List<String> companies, String start, String end) {
        List<StockDataDTO> stockDataDTOS = new ArrayList<>();
        for (String companyCode : companies) {
            RequestParameters parameters = fillParameters(companyCode, start, end);
            stockDataDTOS.addAll(stockDataRetrievalProcessor.retrievalProcess(parameters));
        }
        return convertToNormalizedSortedData(stockDataDTOS);
    }

    private List<StockNormalizedDTO> convertToNormalizedSortedData(List<StockDataDTO> stockDataDTOS) {
        return stockDataDTOS.stream()
                .map(stockNormalizeDTOConverter::convert)
                .sorted(Comparator.comparing((StockNormalizedDTO stockNormalizedDTO) ->
                                stockNormalizedDTO != null ? stockNormalizedDTO.getNormalizedValue()
                                        : BigDecimal.ZERO)
                        .reversed())
                .toList();
    }

    private RequestParameters fillParameters(
            String companyCode, String start, String end) {
        return new RequestParameters(
                companyCode,
                NYSEResultFrequency.DAILY,
                LocalDate.parse(start, NYSEConstants.DATE_FORMAT),
                LocalDate.parse(end, NYSEConstants.DATE_FORMAT));
    }

}
