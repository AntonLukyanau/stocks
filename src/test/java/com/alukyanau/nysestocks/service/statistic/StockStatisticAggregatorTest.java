package com.alukyanau.nysestocks.service.statistic;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.dto.StockStatistic;
import com.alukyanau.nysestocks.service.statistic.StockStatisticAggregator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockStatisticAggregatorTest {

    private final StockStatisticAggregator stockStatisticAggregator = new StockStatisticAggregator();

    @Test
    void testAggregateSuccess() {
        // given
        LocalDate oldestDate = LocalDate.of(2022, 1, 2);
        LocalDate newestDate = LocalDate.of(2022, 1, 4);
        BigDecimal biggestPrice = BigDecimal.valueOf(350.0);
        BigDecimal lowestPrice = BigDecimal.valueOf(100.0);
        String company = "epam";
        StockDataDTO stock1 = StockDataDTO.builder()
                .companyCode(company)
                .date(oldestDate)
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(lowestPrice)
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO stock2 = StockDataDTO.builder()
                .companyCode(company)
                .date(LocalDate.of(2022, 1, 3))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(250.0))
                .minPrice(lowestPrice)
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO stock3 = StockDataDTO.builder()
                .companyCode(company)
                .date(newestDate)
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(biggestPrice)
                .minPrice(BigDecimal.valueOf(150.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        Set<StockDataDTO> stocks = Set.of(stock1, stock2, stock3);
        // when
        StockStatistic stockStatistic = stockStatisticAggregator.aggregate(stocks);
        // then
        assertEquals(company, stockStatistic.getCompany());
        assertEquals(newestDate, stockStatistic.getNewest());
        assertEquals(oldestDate, stockStatistic.getOldest());
        assertEquals(biggestPrice, stockStatistic.getMax());
        assertEquals(lowestPrice, stockStatistic.getMin());
    }

}