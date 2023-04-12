package com.alukyanau.nysestocks.service.resolving;

import com.alukyanau.nysestocks.dto.NormalizedStockData;
import com.alukyanau.nysestocks.dto.StockDataDTO;
import com.alukyanau.nysestocks.model.RequestParameters;
import com.alukyanau.nysestocks.service.DataRetrievalProcessor;
import com.alukyanau.nysestocks.service.ParameterService;
import com.alukyanau.nysestocks.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NormalizeSortedDateServiceTest {

    @InjectMocks
    private NormalizeSortedDateService normalizeSortedDateService;

    @Mock
    private ParameterService<RequestParameters> stockParameterService;
    @Mock
    private DataRetrievalProcessor<List<StockDataDTO>> stockDataRetrievalProcessor;
    @Mock
    private DateUtil dateUtil;

    @Test
    public void testGetNormalizedDTOS() {
        // given
        StockDataDTO aapl1 = StockDataDTO.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 1, 1))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(200.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        StockDataDTO aapl2 = StockDataDTO.builder()
                .companyCode("aapl")
                .date(LocalDate.of(2022, 1, 1))
                .startPrice(BigDecimal.valueOf(150.0))
                .maxPrice(BigDecimal.valueOf(250.0))
                .minPrice(BigDecimal.valueOf(100.0))
                .endPrice(BigDecimal.valueOf(180.0))
                .volume(50000L)
                .build();
        List<StockDataDTO> stockDataDTOs = List.of(aapl1, aapl2);
        NormalizedStockData e1 = new NormalizedStockData(aapl1, BigDecimal.valueOf(1));
        NormalizedStockData e2 = new NormalizedStockData(aapl2, BigDecimal.valueOf(1.5));
        List<NormalizedStockData> normalizedStockDataItems = List.of(e2, e1);
        RequestParameters parameters = new RequestParameters(
                "aapl",
                LocalDate.now(),
                LocalDate.now().minusDays(1));
        // when
        // then

    }
}