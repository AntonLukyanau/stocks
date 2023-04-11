package com.alukyanau.nysestocks.service;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.dto.StockNormalizedDTO;
import com.alukyanau.nysestocks.util.RequestParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NormalizeSortedDateService implements NormalizeDateService {

    private final ParameterService<RequestParameters> stockParameterService;
    private final DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    private final Converter<StockDataDTO, StockNormalizedDTO> stockNormalizeDTOConverter;

    public List<StockNormalizedDTO> getNormalizedDTOS(List<String> companies, String start, String end) {
        List<StockDataDTO> stockDataDTOS = new ArrayList<>();
        for (String companyCode : companies) {
            RequestParameters parameters = stockParameterService.fillParameters(companyCode, start, end);
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

}
