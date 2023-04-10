package com.example.stonks.service;

import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.dto.StockStatistic;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

@Service
public class StockStatisticService implements StatisticService<StockStatistic, StockDataDTO> {

    @Override
    public StockStatistic aggregate(Collection<StockDataDTO> stocks) {
        return StockStatistic.builder()
                .company(resolveCompany(stocks))
                .newest(resolveLatestDate(stocks))
                .oldest(resolveEarliestDate(stocks))
                .min(resolveLowest(stocks))
                .max(resolveHighest(stocks))
                .build();
    }

    private String resolveCompany(Collection<StockDataDTO> stocks) {
        return stocks.stream()
                .findAny()
                .map(StockDataDTO::getCompanyCode)
                .orElse("undefined");
    }

    private LocalDate resolveEarliestDate(Collection<StockDataDTO> stocks) {
        return stocks.stream()
                .map(StockDataDTO::getDate)
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());
    }

    private LocalDate resolveLatestDate(Collection<StockDataDTO> stocks) {
        return stocks.stream()
                .map(StockDataDTO::getDate)
                .max(LocalDate::compareTo)
                .orElse(LocalDate.now());
    }

    private BigDecimal resolveLowest(Collection<StockDataDTO> stocks) {
        return stocks.stream()
                .map(StockDataDTO::getMinPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal resolveHighest(Collection<StockDataDTO> stocks) {
        return stocks.stream()
                .map(StockDataDTO::getMaxPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

}
