package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.dto.StockNormalizedDTO;
import com.example.stonks.util.NYSEConstants;
import com.example.stonks.util.RequestParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NormalizeSortedDateService implements NormalizeDateService {

    private final WorkDaysResolver workDaysResolver;
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
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(StockNormalizedDTO::getNormalizedValue).reversed())
                .toList();
    }

    private RequestParameters fillParameters(String companyCode, String start, String end) {
        LocalDate endDate = end == null || end.isBlank() ? LocalDate.now()
                : LocalDate.parse(end, NYSEConstants.DATE_FORMAT);
        LocalDate startDate = start == null || start.isBlank() ? workDaysResolver.resolveLastWorkDayBefore(endDate)
                : LocalDate.parse(start, NYSEConstants.DATE_FORMAT);
        return new RequestParameters(companyCode, NYSEResultFrequency.DAILY, startDate, endDate);
    }

}
