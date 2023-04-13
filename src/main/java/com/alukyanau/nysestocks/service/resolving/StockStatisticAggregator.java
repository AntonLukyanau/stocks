package com.alukyanau.nysestocks.service.resolving;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.service.StatisticAggregator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Calculate statistics from collection of stock data
 */
@Service
public class StockStatisticAggregator implements StatisticAggregator<StockStatistic, StockDataDTO> {

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
