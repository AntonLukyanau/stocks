package com.example.stonks.service;

import com.example.stonks.dto.ResultFrequency;
import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.dto.wrapper.StockDataWrapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component
public class StockDataParser implements Parser<StockDataDTO, StockDataWrapper> {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);

    @Override
    public List<StockDataDTO> parse(StockDataWrapper wrapper) {
        List<StockDataDTO> stockDataList = new ArrayList<>();
        String company = wrapper.company();
        ResultFrequency resultFrequency = wrapper.resultFrequency();
        String csvData = wrapper.source();
        String[] rows = csvData.split("\n");
        if (isIncorrectHeader(rows[0])) {
            return Collections.emptyList();
        }
        try {
            for (int i = 1; i < rows.length; i++) {
                String[] values = rows[i].split(",");
                String volume = squashVolumeValues(values);
                StockDataDTO stockData = StockDataDTO.builder()
                        .companyCode(company)
                        .date(LocalDate.parse(values[0], DATE_FORMAT))
                        .startPrice(BigDecimal.valueOf(Double.parseDouble(values[1])))
                        .maxPrice(BigDecimal.valueOf(Double.parseDouble(values[2])))
                        .minPrice(BigDecimal.valueOf(Double.parseDouble(values[3])))
                        .endPrice(BigDecimal.valueOf(Double.parseDouble(values[4])))
                        .resultFrequency(resultFrequency)
                        .volume(parseVolume(volume))
                        .build();
                stockDataList.add(stockData);
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            return Collections.emptyList();
        }
        return stockDataList;
    }

    private static String squashVolumeValues(String[] values) {
        StringBuilder volume = new StringBuilder(values[5]);
        for (int j = 6; j < values.length; j++) {
            volume.append(",").append(values[j]);
        }
        return volume.toString();
    }

    private long parseVolume(String volumeString) {
        String sanitizedString = volumeString.replace(",", "");
        return Long.parseLong(sanitizedString);
    }

    private boolean isIncorrectHeader(String header) {
        return !header.equals("Date,Open,High,Low,Close,Volume");
    }

}
