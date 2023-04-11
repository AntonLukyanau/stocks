package com.alukyanau.nysestocks.dto.converter;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.entity.StockData;
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
