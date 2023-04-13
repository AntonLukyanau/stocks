package com.alukyanau.nysestocks.dto.converter;

import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.entity.StockData;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

/**
 * Convert stock data to dto
 *
 * @see StockData
 * @see StockDataDTO
 */
@Service
public class StockDataDTOConverter implements Converter<StockData, StockDataDTO> {

    @Override
    public StockDataDTO convert(StockData source) {
        return StockDataDTO.builder()
                .companyCode(source.getCompanyCode())
                .date(source.getDate())
                .startPrice(source.getStartPrice())
                .maxPrice(source.getMaxPrice())
                .minPrice(source.getMinPrice())
                .endPrice(source.getEndPrice())
                .volume(source.getVolume())
                .build();
    }
}
