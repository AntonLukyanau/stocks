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
public class StockStatistic {

    /**
     * Company
     */
    private String company;
    /**
     * Data was aggregated to this date
     */
    private LocalDate newest;
    /**
     * Data was aggregated from this date
     */
    private LocalDate oldest;
    /**
     * Max price of stock during date range
     */
    private BigDecimal max;
    /**
     * Min price of stock during date range
     */
    private BigDecimal min;

}
