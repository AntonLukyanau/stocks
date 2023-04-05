package com.example.stonks.controller;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.dto.StockNormalizedDTO;
import com.example.stonks.service.DataRetrievalProcessor;
import com.example.stonks.util.NYSEConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockDataController {

    @Value("${stock.companies}")
    private List<String> companies;

    private final DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    private final Converter<StockDataDTO, StockNormalizedDTO> stockNormalizeDTOConverter;

    @GetMapping("/{companyCode}/period")
    public ResponseEntity<List<StockNormalizedDTO>> getStocksDataByCompany(
            @PathVariable String companyCode,
            @RequestParam("startdate") String start,
            @RequestParam("enddate") String end
    ) {
        Map<String, Object> parameters = fillParameters(companyCode, start, end);
        List<StockDataDTO> stockDataDTOS = stockDataRetrievalProcessor.retrievalProcess(parameters);
        List<StockNormalizedDTO> normalizedDTOS = convertToNormalizedSortedData(stockDataDTOS);
        return ResponseEntity.ok(normalizedDTOS);
    }

    @GetMapping("/all/period")
    public ResponseEntity<List<StockNormalizedDTO>> getAllCompanyStocksData(
            @RequestParam("startdate") String start,
            @RequestParam("enddate") String end
    ) {
        List<StockNormalizedDTO> normalizedDTOS = getSortedNormalizedDTOS(start, end);
        return ResponseEntity.ok(normalizedDTOS);
    }

    @GetMapping("/all/highest")
    public ResponseEntity<StockNormalizedDTO> getHighestStockBySpecificDay(@RequestParam("date") String date) {
        Optional<StockNormalizedDTO> highestNormalizedDTO = getSortedNormalizedDTOS(date, date).stream().findFirst();
        return highestNormalizedDTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    private List<StockNormalizedDTO> getSortedNormalizedDTOS(String start, String end) {
        List<StockDataDTO> stockDataDTOS = new ArrayList<>();
        for (String companyCode : companies) {
            Map<String, Object> parameters = fillParameters(companyCode, start, end);
            stockDataDTOS.addAll(stockDataRetrievalProcessor.retrievalProcess(parameters));
        }
        return convertToNormalizedSortedData(stockDataDTOS);
    }

    private static Map<String, Object> fillParameters(String companyCode, String start, String end) {
        return Map.of(
                NYSEConstants.COMPANY_PARAMETER, companyCode,
                NYSEConstants.FREQUENCY_PARAMETER, NYSEResultFrequency.DAILY,
                NYSEConstants.START_DATE, start,
                NYSEConstants.END_DATE, end
        );
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

}
