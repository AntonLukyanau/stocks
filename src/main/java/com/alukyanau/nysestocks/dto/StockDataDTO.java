package com.alukyanau.nysestocks.dto;

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

    /**
     * Company code which represented on NYSE
     */
    private String companyCode;
    /**
     * Date of actuality this data
     */
    private LocalDate date;
    /**
     * Stock price at the start of the date
     */
    private BigDecimal startPrice;
    /**
     * Max stock price during the date
     */
    private BigDecimal maxPrice;
    /**
     * Max stock price during the date
     */
    private BigDecimal minPrice;
    /**
     * Stock price at the end of the date
     */
    private BigDecimal endPrice;
    /**
     * Volume of all stocks for the company
     */
    private Long volume;

}
