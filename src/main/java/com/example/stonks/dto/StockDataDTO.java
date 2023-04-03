package com.example.stonks.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StockDataDTO {

    private String companyCode;
    private LocalDate date;
    private BigDecimal startPrice;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private BigDecimal endPrice;
    private ResultFrequency resultFrequency;
    private Long volume;

}
