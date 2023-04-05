package com.example.stonks.dto.converter;

import com.example.stonks.dto.StockDataDTO;
import com.example.stonks.entity.StockData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class StockDataDTOConverter implements Converter<StockDataDTO, StockData> {

    @Override
    public StockData convert(StockDataDTO source) {
        return StockData.builder()
                .companyCode(source.getCompanyCode())
                .date(source.getDate())
                .startPrice(source.getStartPrice())
                .maxPrice(source.getMaxPrice())
                .minPrice(source.getMinPrice())
                .endPrice(source.getEndPrice())
                .resultFrequency(source.getResultFrequency())
                .volume(source.getVolume())
                .build();
    }
}
