package com.example.stonks.dto.converter;

import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.dto.StockNormalizedDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class StockNormalizeDTOConverter implements Converter<StockDataDTO, StockNormalizedDTO> {

    @Override
    public StockNormalizedDTO convert(StockDataDTO source) {
        BigDecimal subtracted = source.getMaxPrice().subtract(source.getMinPrice());
        BigDecimal divided = subtracted.divide(source.getMinPrice(), MathContext.DECIMAL64);
        return new StockNormalizedDTO(source, divided);
    }

}
