package com.alukyanau.nysestocks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NormalizedStockData {

    private StockDataDTO stockDataDTO;
    private BigDecimal normalizedValue;

}
