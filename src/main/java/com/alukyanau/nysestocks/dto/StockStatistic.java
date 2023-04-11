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

    private String company;
    private LocalDate newest;
    private LocalDate oldest;
    private BigDecimal max;
    private BigDecimal min;

}
