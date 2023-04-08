package com.example.stonks.service;

import com.example.stonks.dto.NYSEResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.util.NYSEConstants;
import com.example.stonks.util.StockDataWrap;
import com.example.stonks.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class StockDataParser implements Parser<StockDataDTO, StockDataWrap> {

    private final Validator<String> csvValidator;

    @Override
    public List<StockDataDTO> parse(StockDataWrap wrapper) {
        List<StockDataDTO> stockDataList = new ArrayList<>();
        String company = wrapper.company();
        NYSEResultFrequency resultFrequency = wrapper.resultFrequency();
        String csvData = wrapper.source();
        if (!csvValidator.validate(csvData)) {
            log.warn("Retrieved data from NYSE are invalid." +
                    " Parameters: " + wrapper.company() + " " + wrapper.resultFrequency());
            return Collections.emptyList();
        }
        String[] rows = csvData.split("\n");
        try {
            for (int i = 1; i < rows.length; i++) {
                String[] values = Arrays.stream(rows[i].split(",\""))
                        .map(value -> value.replace("\"", ""))
                        .toArray(String[]::new);
                StockDataDTO stockData = StockDataDTO.builder()
                        .companyCode(company)
                        .date(LocalDate.parse(values[0], NYSEConstants.DATE_FORMAT))
                        .startPrice(BigDecimal.valueOf(Double.parseDouble(values[1])))
                        .maxPrice(BigDecimal.valueOf(Double.parseDouble(values[2])))
                        .minPrice(BigDecimal.valueOf(Double.parseDouble(values[3])))
                        .endPrice(BigDecimal.valueOf(Double.parseDouble(values[4])))
                        .resultFrequency(resultFrequency)
                        .volume(parseVolume(values[5]))
                        .build();
                stockDataList.add(stockData);
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            return Collections.emptyList();
        }
        return stockDataList;
    }

    private long parseVolume(String volumeString) {
        String sanitizedString = volumeString.replace(",", "");
        return Long.parseLong(sanitizedString);
    }

}
