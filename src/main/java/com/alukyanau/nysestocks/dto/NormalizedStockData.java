package com.alukyanau.nysestocks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NormalizedStockData {

    /**
     * Main data about stock
     */
    private StockDataDTO stockDataDTO;
    /**
     * Calculated by the formula (max-min)/min
     */
    private BigDecimal normalizedValue;

}
