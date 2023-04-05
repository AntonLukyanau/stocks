package com.example.stonks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StockNormalizedDTO {

    private StockDataDTO stockDataDTO;
    private BigDecimal normalizedValue;

}
